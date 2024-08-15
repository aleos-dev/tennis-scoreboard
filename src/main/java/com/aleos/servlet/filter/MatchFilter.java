package com.aleos.servlet.filter;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.exception.ResourceForwardingException;
import com.aleos.model.in.MatchFilterCriteria;
import com.aleos.model.in.MatchPayload;
import com.aleos.model.in.PageablePayload;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.PropertiesUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;

@WebFilter("/matches")
public class MatchFilter extends HttpFilter {

    private transient Validator validator;

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        validator = (Validator) locator.getBean("validator");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        switch (req.getMethod().toUpperCase()) {

            case "GET" -> handleGetMethodPayload(req, resp);

            case "POST" -> handlePostMethodPayload(req, resp);

            default -> {/* do nothing */}
        }

        chain.doFilter(req, resp);
    }

    private void handleGetMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        var pageablePayload = getPageablePayload(req);
        var matchFilterCriteria = getMatchFilterCriteria(req);

        validatePayload(pageablePayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("pageablePayload", pageablePayload));

        validatePayload(matchFilterCriteria).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("matchFilterCriteria", matchFilterCriteria));
    }

    private void handlePostMethodPayload(HttpServletRequest req, HttpServletResponse resp) {
        MatchPayload matchPayload = getMatchPayload(req);

        validatePayload(matchPayload).ifPresentOrElse(
                violations -> handlePayloadViolations(req, resp, violations),
                () -> req.setAttribute("matchPayload", matchPayload)
        );
    }

    private PageablePayload getPageablePayload(HttpServletRequest req) {
        return new PageablePayload(
                toInteger(req.getParameter("page")),
                toInteger(req.getParameter("size")),
                Optional.ofNullable(req.getParameter("sortBy"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.default.sortBy").orElse(null)),
                Optional.ofNullable(req.getParameter("sortDirection"))
                        .orElseGet(() -> PropertiesUtil.get("pageable.default.sortDirection").orElse(null)),
                Optional.ofNullable(req.getParameter("before"))
                        .map(this::toInstant)
                        .orElse(Instant.now())
        );
    }

    private MatchFilterCriteria getMatchFilterCriteria(HttpServletRequest req) {
        return new MatchFilterCriteria(
                Optional.ofNullable(req.getParameter("status"))
                        .orElseGet(() -> PropertiesUtil.get("filter.default.matchStatus").orElse(null)),
                req.getParameter("playerName")
        );
    }

    private static MatchPayload getMatchPayload(HttpServletRequest req) {
        return new MatchPayload(
                req.getParameter("playerOneName"),
                req.getParameter("playerTwoName"),
                req.getParameter("matchFormat")
        );
    }


    private <T> void handlePayloadViolations(HttpServletRequest req,
                                             HttpServletResponse resp,
                                             Set<ConstraintViolation<T>> violations
    ) {
        req.setAttribute("violations", violations);
        req.setAttribute("errorMessage", "Validation errors occurred. Please correct them and submit again.");
        try {
            req.getRequestDispatcher("/originPage.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ResourceForwardingException("Error occurred while dispatching request '/originPage.jsp'. " +
                                                  "The target resource could not be reached.", e);
        }
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

    private <T> Optional<Set<ConstraintViolation<T>>> validatePayload(T payload) {
        Set<ConstraintViolation<T>> violations = validator.validate(payload);

        return violations.isEmpty() ? Optional.empty() : Optional.of(violations);
    }
}
