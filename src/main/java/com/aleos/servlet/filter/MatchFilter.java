package com.aleos.servlet.filter;

import com.aleos.exception.InvalidPathInfo;
import com.aleos.model.in.MatchFilterCriteria;
import com.aleos.model.in.MatchPayload;
import com.aleos.model.in.MatchUuidPayload;
import com.aleos.model.in.PageableInfo;
import com.aleos.util.PropertiesUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/matches")
public class MatchFilter extends AbstractEndpointFilter {

    private static final Logger logger = Logger.getLogger(MatchFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        switch (req.getMethod()) {

            case "GET" -> handleGetMethodPayload(req, resp);

            case "POST" -> handlePostMethodPayload(req, resp);

            default -> {/* do nothing */}
        }

        if (req.getAttribute("violations") == null) {
            chain.doFilter(req, resp);
        } else {

            logger.log(Level.SEVERE, req.getAttribute("violations").toString());
        }
    }

    private void handleGetMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo() == null) {

            // -> /matches
            extractPageablePayloadToReqContext(req, resp);
            extractMatchFilterCriteriaToReqContext(req, resp);

        } else {

            // -> /matches/{uuid}
            extractMatchUuidPayloadToReqContext(req);
        }
    }

    private void handlePostMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        extractMatchPayloadToReqContext(req, resp);
    }

    private void extractMatchUuidPayloadToReqContext(HttpServletRequest req) {
        var matchUuidPayload = getMatchUuidPayload(req);
        req.setAttribute("matchUuidPayload", matchUuidPayload);
    }

    private void extractMatchFilterCriteriaToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        var matchFilterCriteria = getMatchFilterCriteria(req);

        validatePayload(matchFilterCriteria).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("matchFilterCriteria", matchFilterCriteria));
    }


    private void extractPageablePayloadToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        var pageablePayload = getPageablePayload(req);

        validatePayload(pageablePayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("pageablePayload", pageablePayload));
    }

    private void extractMatchPayloadToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        MatchPayload matchPayload = getMatchPayload(req);

        validatePayload(matchPayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("matchPayload", matchPayload)
        );
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

    private MatchPayload getMatchPayload(HttpServletRequest req) {
        return new MatchPayload(
                req.getParameter("playerOneName"),
                req.getParameter("playerTwoName"),
                req.getParameter("matchFormat")
        );
    }

    private PageableInfo getPageablePayload(HttpServletRequest req) {
        return PageableInfo.of(
                toInteger(Optional.ofNullable(req.getParameter("page"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.default.page").orElse("1"))),
                toInteger(Optional.ofNullable(req.getParameter("size"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.default.size").orElse("2"))),
                Optional.ofNullable(req.getParameter("sortBy"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.match.default.sortBy").orElse(null)),
                Optional.ofNullable(req.getParameter("sortDirection"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.match.default.sortDirection").orElse(null))
        );
    }

    private MatchFilterCriteria getMatchFilterCriteria(HttpServletRequest req) {
        return new MatchFilterCriteria(
                Optional.ofNullable(req.getParameter("status"))
                        .orElseGet(() -> PropertiesUtil.get("filter.default.matchStatus").orElse(null)),
                req.getParameter("playerName"),
                Optional.ofNullable(req.getParameter("instant"))
                        .map(this::toInstant)
                        .orElse(Instant.now())
        );
    }
}
