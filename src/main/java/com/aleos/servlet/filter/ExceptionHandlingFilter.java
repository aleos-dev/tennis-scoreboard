package com.aleos.servlet.filter;

import com.aleos.exception.ImageServiceException;
import com.aleos.exception.InvalidPageableRequest;
import com.aleos.exception.InvalidPathInfo;
import com.aleos.exception.PlayerRegistrationException;
import com.aleos.util.ServletUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandlingFilter extends HttpFilter {

    private static final Logger logger = Logger.getLogger(ExceptionHandlingFilter.class.getName());

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException {
        try {
            chain.doFilter(req, resp);

        } catch (InvalidPageableRequest
                 | InvalidPathInfo
                 | PlayerRegistrationException
                 | ImageServiceException e) {

            logger.log(Level.SEVERE, e.getMessage(), e);
            req.setAttribute("errorMessages", List.of(e.getMessage()));
            ServletUtil.forward(req, resp, req.getServletPath());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Something went wrong!", e);
            resp.sendError(500, "Oops! Looks like the gremlins are at it again. We'll fix it soon!");
        }
    }
}
