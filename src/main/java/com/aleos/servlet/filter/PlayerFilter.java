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
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerFilter extends AbstractEndpointFilter {

    private static final Logger logger = Logger.getLogger(PlayerFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        switch (req.getMethod()) {

            case "GET" -> handleGetMethodPayload(req, resp);

            case "POST" -> handlePostMethodPayload(req, resp);

            case "PUT" -> handlePutMethodPayload(req, resp);

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

            extractPageablePayloadToReqContext(req, resp);
            extractFilterCriteriaToReqContext(req, resp);

        } else {
            extractPlayerNamePayloadToReqContext(req, resp);
        }
    }

    private void handlePostMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        extractPlayerPayloadToReqContext(req, resp);
    }

    private void handlePutMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        extractPlayerIdentifierToReqContext(req, resp);
        extractPlayerPayloadToReqContext(req, resp);

    }

    private void extractPlayerIdentifierToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo() == null) {
            throw new InvalidPathInfo("The path info must contain UUID match identifier.");
        }

        int skipSlash = 1;
        var payload = new PlayerNamePayload(req.getPathInfo().substring(skipSlash));
        validatePayload(payload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("playerNamePayload", payload)
        );
        req.setAttribute("playerNamePayload", payload);
    }

    private void extractPlayerNamePayloadToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        PlayerNamePayload payload = getPlayerNamePayload(req);

        validatePayload(payload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("playerNamePayload", payload)
        );
    }

    private void extractFilterCriteriaToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        var playerFilterCriteria = getPlayerFilterCriteria(req);


        validatePayload(playerFilterCriteria).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("playerFilterCriteria", playerFilterCriteria));
    }

    private void extractPageablePayloadToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        var pageablePayload = getPageablePayload(req);
        validatePayload(pageablePayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("pageablePayload", pageablePayload));
    }

    private void extractPlayerPayloadToReqContext(HttpServletRequest req, HttpServletResponse resp) {
        PlayerPayload payload = getPlayerPayload(req);

        validatePayload(payload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("playerPayload", payload)
        );
    }

    private PlayerPayload getPlayerPayload(HttpServletRequest req) {
        return new PlayerPayload(
                req.getParameter("name"),
                req.getParameter("country"),
                req.getParameter("imageUrl")
        );
    }

    private PlayerNamePayload getPlayerNamePayload(HttpServletRequest req) {
        return new PlayerNamePayload(req.getParameter("name"));
    }

    private PlayerFilterCriteria getPlayerFilterCriteria(HttpServletRequest req) {
        return new PlayerFilterCriteria(
                req.getParameter("country"),
                req.getParameter("name"),
                Optional.ofNullable(req.getParameter("instant"))
                        .map(this::toInstant)
                        .orElse(Instant.now())
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
