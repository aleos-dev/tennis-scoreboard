package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.service.PlayerService;
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

@WebServlet("/players/*")
public class PlayerServlet extends HttpServlet {

    private transient PlayerService playerService;

    private transient ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        playerService = (PlayerService) locator.getBean("playerService");
        objectMapper = (ObjectMapper) locator.getBean("objectMapper");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        var payload = (PlayerNamePayload) request.getAttribute("playerNamePayload");
        try {

            var players = playerService.findByName(payload);

            String asString = objectMapper.writeValueAsString(players.get());
            response.getWriter().write(asString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp) {
        var identifier = req.getAttribute("playerNamePayload");
        var payload = req.getAttribute("playerPayload");

        playerService.update((PlayerNamePayload) identifier, (PlayerPayload) payload);
        resp.setStatus(204);
    }
}
