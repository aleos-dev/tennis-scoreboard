package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.in.PageableInfo;
import com.aleos.model.in.PlayerFilterCriteria;
import com.aleos.model.in.PlayerPayload;
import com.aleos.model.out.PlayersDto;
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

@WebServlet("/players")
public class PlayersServlet extends HttpServlet {

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
        try {

            var pageable = (PageableInfo) request.getAttribute("pageablePayload");
            var filterCriteria = (PlayerFilterCriteria) request.getAttribute("playerFilterCriteria");

            PlayersDto all = playerService.findAll(pageable, filterCriteria);

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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            var payload = (PlayerPayload) request.getAttribute("playerPayload");

            playerService.createPlayer(payload);

        } catch(Exception e ) {

        }
    }
}
