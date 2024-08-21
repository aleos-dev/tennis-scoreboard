package com.aleos.servlet.filter;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.exception.JspForwardingException;
import com.aleos.servicelocator.ServiceLocator;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
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

public abstract class AbstractEndpointFilter extends HttpFilter {

    protected transient Validator validator;

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        validator = (Validator) locator.getBean("validator");
    }

    protected <T> Optional<Set<ConstraintViolation<T>>> validatePayload(T payload) {
        Set<ConstraintViolation<T>> violations = validator.validate(payload);

        return violations.isEmpty() ? Optional.empty() : Optional.of(violations);
    }


    protected Instant toInstant(String before) {
        try {
            return Instant.parse(before);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    protected Integer toInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected <T> void handlePayloadViolations(HttpServletRequest req,
                                               HttpServletResponse resp,
                                               Set<ConstraintViolation<T>> violations
    ) {
        req.setAttribute("violations", violations);
        req.setAttribute("errorMessage", "Validation errors occurred. Please correct them and submit again.");
        try {
            req.getRequestDispatcher("/errorPage.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new JspForwardingException("Error occurred while dispatching request '/originPage.jsp'. " +
                                             "The target resource could not be reached.", e);
        }
    }

    protected boolean isRequestForMainPath(HttpServletRequest req) {
        return req.getPathInfo() == null;
    }
}
