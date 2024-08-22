package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.MatchScore;
import com.aleos.model.dto.in.MatchUuidPayload;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.model.dto.out.ConcludedMatchDto;
import com.aleos.service.MatchService;
import com.aleos.service.ScoreTrackerService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.function.Consumer;

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

        trackerService.findById(uuidPayload.id())
                .ifPresentOrElse(
                        handleOngoingMatch(req, resp),
                        () -> handleConcludedMatch(req, resp, uuidPayload)
                );

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var matchUuidPayload = (MatchUuidPayload) req.getAttribute("matchUuidPayload");
        var playerNamePayload = (PlayerNamePayload) req.getAttribute("playerNamePayload");

        matchService.scorePoint(matchUuidPayload, playerNamePayload);


        ServletUtil.redirect(req, resp, matchUuidPayload.id().toString());
    }

    private Consumer<MatchScore> handleOngoingMatch(HttpServletRequest req, HttpServletResponse resp) {
        return score -> {
            req.setAttribute("matchScore", score);
            req.setAttribute("notifications", score.getNotifications());
            ServletUtil.forwardToJsp(req, resp, "control/live-match");
        };
    }

    private void handleConcludedMatch(HttpServletRequest req, HttpServletResponse resp, MatchUuidPayload uuidPayload) {
        matchService.findById(uuidPayload)
                .filter(ConcludedMatchDto.class::isInstance)
                .map(ConcludedMatchDto.class::cast)
                .ifPresent(concludedMatch -> req.setAttribute("concludedMatch", concludedMatch));

        ServletUtil.forwardToJsp(req, resp, "display/completed-match");
    }
}
