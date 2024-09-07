package com.aleos.servlet.filter;

import com.aleos.exception.InvalidPathInfo;
import com.aleos.model.dto.in.PageableInfo;
import com.aleos.model.dto.in.PlayerFilterCriteria;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.util.PropertiesUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerFilter extends AbstractEndpointFilter {

    private static final Logger logger = Logger.getLogger(PlayerFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        switch (req.getMethod()) {

            case "GET" -> handleGetMethodPayload(req);

            case "POST" -> handlePostMethodPayload(req);

            default -> { /*do nothing*/}
        }

        if (req.getAttribute("errorMessages") != null) {
            logger.log(Level.SEVERE, () -> req.getAttribute("errorMessages").toString());
        }

        chain.doFilter(req, resp);
    }

    private void handleGetMethodPayload(HttpServletRequest req) {
        if (isRequestForMainPath(req)) {

            extractPageablePayloadToReqContext(req);
            extractFilterCriteriaToReqContext(req);

        } else {
            extractPlayerNamePayloadToReqContext(req);
        }
    }

    private void handlePostMethodPayload(HttpServletRequest req) {
        if (!isRequestForMainPath(req)) {
            extractPlayerIdentifierToReqContext(req);
        }
        extractPlayerPayloadToReqContext(req);
    }

    private void extractPlayerIdentifierToReqContext(HttpServletRequest req) {
        if (req.getPathInfo() == null) {
            throw new InvalidPathInfo("The path info must contain UUID match identifier.");
        }

        int skipSlash = 1;
        var payload = new PlayerNamePayload(req.getPathInfo().substring(skipSlash));

        validatePayload(payload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("playerNamePayload", payload)
        );
    }

    private void extractPlayerNamePayloadToReqContext(HttpServletRequest req) {
        PlayerNamePayload payload = getPlayerNamePayload(req);

        validatePayload(payload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("playerNamePayload", payload)
        );
    }

    private void extractFilterCriteriaToReqContext(HttpServletRequest req) {
        var playerFilterCriteria = getPlayerFilterCriteria(req);


        validatePayload(playerFilterCriteria).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("playerFilterCriteria", playerFilterCriteria));
    }

    private void extractPageablePayloadToReqContext(HttpServletRequest req) {
        var pageablePayload = getPageablePayload(req);
        validatePayload(pageablePayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("pageablePayload", pageablePayload));
    }

    private void extractPlayerPayloadToReqContext(HttpServletRequest req) {
        PlayerPayload payload = getPlayerPayload(req);

        req.setAttribute("playerPayload", payload);
        validatePayload(payload).ifPresent(violations -> handlePayloadViolations(req, violations));
    }

    private PlayerPayload getPlayerPayload(HttpServletRequest req) {
        String playerName = req.getParameter("name");
        String country = req.getParameter("country");
        return new PlayerPayload(
                playerName == null ? null : playerName.trim(),
                country == null ? null : country.trim().toUpperCase()
        );
    }

    private PlayerNamePayload getPlayerNamePayload(HttpServletRequest req) {
        var skipSlash = 1;
        var name = req.getPathInfo().substring(skipSlash).trim();
        var decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);

        return new PlayerNamePayload(decoded);
    }

    private PlayerFilterCriteria getPlayerFilterCriteria(HttpServletRequest req) {
        var country = req.getParameter("country");
        var name = req.getParameter("name");
        var beforeDate = req.getParameter("before");

        return new PlayerFilterCriteria(
                country == null || country.isBlank() ? null : country.trim().toUpperCase(),
                name == null || name.isBlank() ? null : name.trim(),
                Optional.ofNullable(beforeDate).map(this::toInstant).orElse(Instant.now())
        );
    }

    private PageableInfo getPageablePayload(HttpServletRequest req) {
        return PageableInfo.of(
                toInteger(Optional.ofNullable(req.getParameter("page"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.default.page").orElse("1"))),
                toInteger(Optional.ofNullable(req.getParameter("size"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.default.size").orElse("2"))),
                Optional.ofNullable(req.getParameter("sortBy"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.player.default.sortBy").orElse(null)),
                Optional.ofNullable(req.getParameter("sortDirection"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.player.default.sortDirection").orElse(null))
        );
    }
}
