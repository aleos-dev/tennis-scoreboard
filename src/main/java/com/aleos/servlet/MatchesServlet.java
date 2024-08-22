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
        var pageable = (PageableInfo) req.getAttribute("pageablePayload");
        var filterCriteria = (MatchFilterCriteria) req.getAttribute("matchFilterCriteria");

        MatchesDto matchesDto = matchService.findAll(pageable, filterCriteria);
        req.setAttribute("matchesDto", matchesDto);

        ServletUtil.forwardToJsp(req, resp, "control/matches");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var payload = (MatchPayload) req.getAttribute("matchPayload");

        UUID matchId = matchService.createMatch(payload);

        ServletUtil.redirect(req, resp, "/match-scores/%s".formatted(matchId));
    }
}
