package com.aleos.servlet.filter;

import com.aleos.exception.InvalidPathInfo;
import com.aleos.model.dto.in.MatchFilterCriteria;
import com.aleos.model.dto.in.MatchPayload;
import com.aleos.model.dto.in.MatchUuidPayload;
import com.aleos.model.dto.in.PageableInfo;
import com.aleos.util.PropertiesUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchFilter extends AbstractEndpointFilter {

    private static final Logger logger = Logger.getLogger(MatchFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        switch (req.getMethod()) {

            case "GET" -> handleGetMethodPayload(req);

            case "POST" -> handlePostMethodPayload(req);

            default -> {/* do nothing */}
        }

        if (req.getAttribute("violations") != null) {
            logger.log(Level.SEVERE, req.getAttribute("violations").toString());
        }

        chain.doFilter(req, resp);
    }

    private void handleGetMethodPayload(HttpServletRequest req) {
        if (isRequestForMainPath(req)) {

            // -> /matches
            extractPageablePayloadToReqContext(req);
            extractMatchFilterCriteriaToReqContext(req);

        } else {

            // -> /matches/{uuid}
            extractMatchUuidPayloadToReqContext(req);
        }
    }

    private void handlePostMethodPayload(HttpServletRequest req) {
        extractMatchPayloadToReqContext(req);
    }

    private void extractMatchUuidPayloadToReqContext(HttpServletRequest req) {
        var matchUuidPayload = getMatchUuidPayload(req);
        req.setAttribute("matchUuidPayload", matchUuidPayload);
    }

    private void extractMatchFilterCriteriaToReqContext(HttpServletRequest req) {
        var matchFilterCriteria = getMatchFilterCriteria(req);

        validatePayload(matchFilterCriteria).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("matchFilterCriteria", matchFilterCriteria));
    }


    private void extractPageablePayloadToReqContext(HttpServletRequest req) {
        var pageablePayload = getPageablePayload(req);

        validatePayload(pageablePayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("pageablePayload", pageablePayload));
    }

    private void extractMatchPayloadToReqContext(HttpServletRequest req) {
        MatchPayload matchPayload = getMatchPayload(req);

        validatePayload(matchPayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
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
        var name = req.getParameter("playerName");
        return new MatchFilterCriteria(
                Optional.ofNullable(req.getParameter("status"))
                        .orElseGet(() -> PropertiesUtil.get("filter.default.matchStatus").orElse(null)),
                name != null && name.isBlank() ? null : name,
                Optional.ofNullable(req.getParameter("before"))
                        .map(this::toInstant)
                        .orElse(Instant.now())
        );
    }
}
