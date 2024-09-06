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

import java.time.Instant;
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
        if (ServletUtil.checkErrors(req, resp, "control/matches")) {
            return;
        }
        var pageable = (PageableInfo) req.getAttribute("pageablePayload");
        var filterCriteria = (MatchFilterCriteria) req.getAttribute("matchFilterCriteria");

        MatchesDto matchesDto = matchService.findAll(pageable, filterCriteria);

        req.setAttribute("matchesDto", matchesDto);
        req.setAttribute("filterCriteria", filterCriteria);
        req.setAttribute("pageable", pageable);
        req.setAttribute("baseUrl", req.getContextPath() + req.getServletPath());
        req.setAttribute("queryParam", buildQueryString(filterCriteria));
        req.setAttribute("formattedBefore", formatInstantForHtmlForm(filterCriteria.before()));

        ServletUtil.forwardToJsp(req, resp, "control/matches");
    }

    private String formatInstantForHtmlForm(Instant before) {
        return before != null
                ? ServletUtil.formatInstantAsLocalDateTime(before).substring(0, 16)
                : null;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (ServletUtil.checkErrors(req, resp, "control/create-match")) {
            return;
        }

        UUID matchId = matchService.createMatch(
                (MatchPayload) req.getAttribute("matchPayload")
        );

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

        if (filterCriteria.before() != null) {
            queryParams.add("before=" + ServletUtil.formatInstantAsLocalDateTime(filterCriteria.before()));
        }

        return queryParams.toString();
    }
}
