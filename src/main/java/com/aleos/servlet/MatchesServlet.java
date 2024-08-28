package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.MatchFilterCriteria;
import com.aleos.model.dto.in.MatchPayload;
import com.aleos.model.dto.in.PageableInfo;
import com.aleos.model.dto.out.MatchesDto;
import com.aleos.service.MatchService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.UUID;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

    private transient MatchService matchService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        matchService = (MatchService) locator.getBean("matchService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getAttribute("violations") != null) {
            ServletUtil.forwardToJsp(req, resp, "control/matches");
            return;
        }

        var pageable = (PageableInfo) req.getAttribute("pageablePayload");
        var filterCriteria = (MatchFilterCriteria) req.getAttribute("matchFilterCriteria");


        MatchesDto matchesDto = matchService.findAll(pageable, filterCriteria);
        req.setAttribute("matchesDto", matchesDto);
        req.setAttribute("filterCriteria", filterCriteria);
        req.setAttribute("pageable", pageable);

        // Build the query parameters (excluding pagination)
        req.setAttribute("baseUrl", req.getContextPath() + req.getServletPath());
        req.setAttribute("queryParam", buildQueryString(filterCriteria));

        ServletUtil.forwardToJsp(req, resp, "control/matches");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getAttribute("violations") != null) {
            ServletUtil.forwardToJsp(req, resp, "control/create-match");
            return;
        }

        var payload = (MatchPayload) req.getAttribute("matchPayload");
        UUID matchId = matchService.createMatch(payload);

        ServletUtil.redirect(req, resp, "/matches/%s".formatted(matchId));
    }

    private String buildQueryString(MatchFilterCriteria filterCriteria) {
        StringJoiner queryParams = new StringJoiner("&", "?", "");

        if (filterCriteria.playerName() != null && !filterCriteria.playerName().isEmpty()) {
            queryParams.add("playerName=" + filterCriteria.playerName());
        }
        if (filterCriteria.status() != null && !filterCriteria.status().isEmpty()) {
            queryParams.add("status=" + filterCriteria.status());
        }

        queryParams.add("before=" + getBeforeDateAttribute(filterCriteria));

        return queryParams.toString();
    }

    private String getBeforeDateAttribute(MatchFilterCriteria filterCriteria) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(filterCriteria.before());
    }
}
