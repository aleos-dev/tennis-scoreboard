package com.aleos.servlet.filter;

import com.aleos.model.dto.in.PlayerNamePayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AvatarFilter extends AbstractEndpointFilter {

    private static final Logger logger = Logger.getLogger(AvatarFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        if (req.getMethod().equals("GET")) {
            handleGetMethodPayload(req);
        }

        if (req.getAttribute("errorMessages") != null) {
            logger.log(Level.SEVERE, () -> req.getAttribute("errorMessages").toString());
        }

        chain.doFilter(req, resp);
    }

    private void handleGetMethodPayload(HttpServletRequest req) {
            extractPlayerNamePayloadToReqContext(req);
    }

    private void extractPlayerNamePayloadToReqContext(HttpServletRequest req) {
        PlayerNamePayload payload = getPlayerNamePayload(req);

        validatePayload(payload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, violations),
                () -> req.setAttribute("playerNamePayload", payload)
        );
    }

    private PlayerNamePayload getPlayerNamePayload(HttpServletRequest req) {
        var skipSlash = 1;
        var name = req.getPathInfo().substring(skipSlash);
        var decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);

        return new PlayerNamePayload(decoded);
    }
}