package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.MatchUuidPayload;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/matches/*")
public class MatchServlet extends HttpServlet {

    private transient MatchService matchService;

    private transient ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        matchService = (MatchService) locator.getBean("matchService");
        objectMapper = (ObjectMapper) locator.getBean("objectMapper");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {

        Object uuidPayload = req.getAttribute("matchUuidPayload");

        handleGetByUuid((MatchUuidPayload) uuidPayload, resp);
    }

    private void handleGetByUuid(MatchUuidPayload uuidPayload, HttpServletResponse resp) {
        matchService.findById(uuidPayload)
                .ifPresent(match -> makeResponse(resp, match));
    }

    private void makeResponse(HttpServletResponse resp, Object obj) {

        try {
            String asString = objectMapper.writeValueAsString(obj);
            resp.getWriter().write(asString);

            resp.setContentType("application/json");
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
