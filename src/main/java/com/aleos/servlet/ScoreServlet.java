package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.MatchUuidPayload;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.service.MatchService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/match-scores/*")
public class ScoreServlet extends HttpServlet {

    private transient MatchService matchService;

    @Override
    public void init(ServletConfig config) {
        var locator = (ServiceLocator) config.getServletContext().getAttribute(AppContextAttribute.BEAN_FACTORY.name());
        matchService = (MatchService) locator.getBean("matchService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var matchUuidPayload = (MatchUuidPayload) req.getAttribute("matchUuidPayload");
        var playerNamePayload = (PlayerNamePayload) req.getAttribute("playerNamePayload");

        matchService.scorePoint(matchUuidPayload, playerNamePayload);

        ServletUtil.redirect(req, resp, "/matches/%s".formatted(matchUuidPayload.id()));
    }
}
