package com.aleos.servlet.filter;

import com.aleos.model.in.PageablePayload;
import com.aleos.util.PropertiesUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@WebFilter("/matches")
public class MatchFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getMethod().equalsIgnoreCase("GET")) {
            handleGetMethodPayload(req);
        }

        chain.doFilter(req, res);
    }

    private void handleGetMethodPayload(HttpServletRequest req) {
        String sortBy = req.getParameter("sortBy");
        String sortDirection = req.getParameter("sortDirection");
        String before = req.getParameter("before");

        PageablePayload pageable = new PageablePayload(
                toInteger(req.getParameter("page")),
                toInteger(req.getParameter("size")),
                sortBy == null ? PropertiesUtil.get("pageable.default.sortBy").orElse(null) : sortBy,
                sortDirection == null ? PropertiesUtil.get("pageable.default.sortDirection").orElse(null) : sortDirection,
                before == null ? Instant.now() : toInstant(before)
        );

        req.setAttribute("pageable", pageable);
    }

    private Instant toInstant(String before) {
        try {
            return Instant.parse(before);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Integer toInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
