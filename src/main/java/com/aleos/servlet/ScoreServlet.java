package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.MatchScore;
import com.aleos.model.dto.in.MatchUuidPayload;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.service.MatchService;
import com.aleos.service.ScoreTrackerService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

@WebServlet("/match-scores/*")
public class ScoreServlet extends HttpServlet {

    private transient MatchService matchService;

    private transient ScoreTrackerService trackerService;


    @Override
    public void init(ServletConfig config) {
        var locator = (ServiceLocator) config.getServletContext().getAttribute(AppContextAttribute.BEAN_FACTORY.name());
        matchService = (MatchService) locator.getBean("matchService");
        trackerService = (ScoreTrackerService) locator.getBean("scoreTrackerService");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var uuidPayload = (MatchUuidPayload) req.getAttribute("matchUuidPayload");

        Optional<MatchScore> scoreById = trackerService.findById(uuidPayload.id());
        scoreById.ifPresent(score -> req.setAttribute("matchScore", score));

        trackerService.findById(uuidPayload.id())
                .flatMap(MatchScore::pollNotification)
                .ifPresent(message -> req.setAttribute("notification", message));

        ServletUtil.forwardToJsp(req, resp, "control/activeMatch");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var matchUuidPayload = (MatchUuidPayload) req.getAttribute("matchUuidPayload");
        var playerNamePayload = (PlayerNamePayload) req.getAttribute("playerNamePayload");

        matchService.scorePoint(matchUuidPayload, playerNamePayload);


        ServletUtil.redirect(req, resp, matchUuidPayload.id().toString());
    }
}
