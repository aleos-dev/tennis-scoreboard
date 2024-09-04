package com.aleos.servlet.filter;

import com.aleos.exception.InvalidPathInfo;
import com.aleos.model.dto.in.MatchUuidPayload;
import com.aleos.model.dto.in.PlayerNamePayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/match-scores/*")
public class ScoreFilter extends AbstractEndpointFilter {

    private static final Logger logger = Logger.getLogger(ScoreFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        switch (req.getMethod()) {

            case "GET" -> handleGetMethodPayload(req);

            case "POST" -> handlePostMethodPayload(req);

            default -> {/* do nothing */}
        }

        if (req.getAttribute("violations") != null) {
            logger.log(Level.SEVERE, () -> req.getAttribute("violations").toString());
        }

        chain.doFilter(req, resp);
    }

    private void handleGetMethodPayload(HttpServletRequest req) {
        extractMatchUuidPayloadToReqContext(req);
    }

    private void handlePostMethodPayload(HttpServletRequest req) {
        extractMatchUuidPayloadToReqContext(req);
        extractPlayerNamePayloadToReqContext(req);
    }

    private void extractMatchUuidPayloadToReqContext(HttpServletRequest req) {
        var matchUuidPayload = getMatchUuidPayload(req);
        req.setAttribute("matchUuidPayload", matchUuidPayload);
    }


    private MatchUuidPayload getMatchUuidPayload(HttpServletRequest req) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                throw new InvalidPathInfo("The path info should contain uuid for the requested match");
            }

            int skipSlash = 1;
            return new MatchUuidPayload(UUID.fromString(pathInfo.substring(skipSlash)));

        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new InvalidPathInfo("The path info can not be casted to UUID");
        }
    }

    private void extractPlayerNamePayloadToReqContext(HttpServletRequest req) {
        PlayerNamePayload payload = getPlayerNamePayload(req);

        validatePayload(payload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("playerNamePayload", payload)
        );
    }

    private PlayerNamePayload getPlayerNamePayload(HttpServletRequest req) {
        String pointWinner = req.getParameter("pointWinner");
        return new PlayerNamePayload(pointWinner);
    }
}
