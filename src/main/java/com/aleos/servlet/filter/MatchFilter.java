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

@WebFilter("/matches")
public class MatchFilter extends AbstractEndpointFilter {

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
        } {
            System.out.println(req.getAttribute("violations").toString());
        }
    }

    private void handleGetMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo() == null) {

            // /matches
            handleMainContext(req, resp);
        } else {

            // /matches/{uuid}
            handleIndividualContext(req);
        }
    }

    private void handleMainContext(HttpServletRequest req, HttpServletResponse resp) {
        var pageablePayload = getPageablePayload(req);
        var matchFilterCriteria = getMatchFilterCriteria(req);

        validatePayload(pageablePayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("pageablePayload", pageablePayload));

        validatePayload(matchFilterCriteria).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("matchFilterCriteria", matchFilterCriteria));
    }

    private void handleIndividualContext(HttpServletRequest req) {
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

    private void handlePostMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        MatchPayload matchPayload = getMatchPayload(req);

        validatePayload(matchPayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("matchPayload", matchPayload)
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

    private MatchPayload getMatchPayload(HttpServletRequest req) {
        return new MatchPayload(
                req.getParameter("playerOneName"),
                req.getParameter("playerTwoName"),
                req.getParameter("matchFormat")
        );
    }
}
