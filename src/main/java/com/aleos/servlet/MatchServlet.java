package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.in.MatchFilterCriteria;
import com.aleos.model.in.PageablePayload;
import com.aleos.model.out.MatchesDto;
import com.aleos.service.MatchService;
import com.aleos.servicelocator.ServiceLocator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/matches")
public class MatchServlet extends HttpServlet {

    private transient MatchService matchService;

    private transient ObjectMapper objectMapper;

    private transient Validator validator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        matchService = (MatchService) locator.getBean("matchService");
        objectMapper = (ObjectMapper) locator.getBean("objectMapper");
        validator = (Validator) locator.getBean("validator");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {

            var pageable = (PageablePayload) request.getAttribute("pageablePayload");
            var filterCriteria = (MatchFilterCriteria) request.getAttribute("matchFilterCriteria");

            MatchesDto all = matchService.findAll(pageable, filterCriteria);

            String asString = objectMapper.writeValueAsString(all);
            response.getWriter().write(asString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    }
}
