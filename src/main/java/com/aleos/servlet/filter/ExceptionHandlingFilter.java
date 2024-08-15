package com.aleos.servlet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandlingFilter extends HttpFilter {

    private static final Logger logger = Logger.getLogger(ExceptionHandlingFilter.class.getName());

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        try {

            chain.doFilter(req, resp);

        } catch (Exception e) {

            logger.log(Level.SEVERE, e.getMessage(), e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            req.getRequestDispatcher("/errorPage.jsp").forward(req, resp);
        }
    }
}
