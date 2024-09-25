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
import java.util.List;
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

        if (req.getAttribute("errorMessages") != null) {
            logger.log(Level.SEVERE, () -> req.getAttribute("errorMessages").toString());
        }

        chain.doFilter(req, resp);
    }

    private void handleGetMethodPayload(HttpServletRequest req) {
        if (isRequestForMainPath(req)) {

            extractPageablePayloadToReqContext(req);
            extractMatchFilterCriteriaToReqContext(req);

        } else {

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
        var playerOneName = req.getParameter("playerOneName");
        var playerTwoName = req.getParameter("playerTwoName");

        return new MatchPayload(
                playerOneName == null ? null : playerOneName.trim(),
                playerTwoName == null ? null : playerTwoName.trim(),
                req.getParameter("matchFormat")
        );
    }

    private PageableInfo getPageablePayload(HttpServletRequest req) {
        int page = Optional.ofNullable(toInteger(req.getParameter("page"))).orElse(1);
        int size = Optional.ofNullable(toInteger(req.getParameter("size")))
                .orElse(toInteger(PropertiesUtil.get("pageable.default.size").orElse("2")));
        String sortBy = getSortBy(req);
        String sortDirection = Optional.ofNullable(req.getParameter("sortDirection"))
                .orElseGet(() -> PropertiesUtil.get("pageable.match.default.sortDirection").orElse(null));

        return PageableInfo.of(page, size, sortBy, sortDirection);
    }

    private MatchFilterCriteria getMatchFilterCriteria(HttpServletRequest req) {
        var name = req.getParameter("playerName");
        var before = req.getParameter("before");

        return new MatchFilterCriteria(
                Optional.ofNullable(req.getParameter("status"))
                        .orElseGet(() -> PropertiesUtil.get("filter.default.matchStatus").orElse(null)),
                name == null || name.isBlank() ? null : name.trim(),
                before == null || before.isBlank() ? null : toInstant(before)
        );
    }

    private String getSortBy(HttpServletRequest req) {
        var sortBy = req.getParameter("sortBy");
        var allowedSortBy = List.of("concludedAt");

        return sortBy != null && allowedSortBy.contains(sortBy)
                ? sortBy
                : PropertiesUtil.get("pageable.match.default.sortBy").orElse(null);
    }
}
