package com.aleos.servlet.filter;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.servicelocator.ServiceLocator;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractEndpointFilter extends HttpFilter {

    protected transient Validator validator;

    private static final Logger logger = Logger.getLogger(AbstractEndpointFilter.class.getName());

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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime localDateTime = LocalDateTime.parse(before + (before.length() == 16 ? ":59.999" : ""), formatter);

            return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        } catch (DateTimeParseException e) {
            logger.log(Level.SEVERE, "Can not parse before parameter in the filter", e);
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
                                               Set<ConstraintViolation<T>> violations
    ) {
        var messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .toList();

        req.setAttribute("errorMessages", messages);
    }

    protected boolean isRequestForMainPath(HttpServletRequest req) {
        return req.getPathInfo() == null;
    }
}
