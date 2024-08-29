package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.PageableInfo;
import com.aleos.model.dto.in.PlayerFilterCriteria;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.model.dto.out.PlayersDto;
import com.aleos.service.PlayerService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.StringJoiner;

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
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var pageable = (PageableInfo) req.getAttribute("pageablePayload");
        var filterCriteria = (PlayerFilterCriteria) req.getAttribute("playerFilterCriteria");

        PlayersDto playersDto = playerService.findAll(pageable, filterCriteria);
        req.setAttribute("playersDto", playersDto);
        req.setAttribute("filterCriteria", filterCriteria);
        req.setAttribute("pageable", pageable);
        req.setAttribute("countryCodes", List.of("EN", "UA", "RU"));

        // Build the query parameters (excluding pagination)
        req.setAttribute("queryParam", buildQueryString(filterCriteria));
        req.setAttribute("baseUrl", req.getContextPath() + req.getServletPath());

        ServletUtil.forwardToJsp(req, resp, "control/players");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            var payload = (PlayerPayload) request.getAttribute("playerPayload");

            playerService.createPlayer(payload);

        } catch (Exception e) {

        }
    }

    public static String buildQueryString(PlayerFilterCriteria filterCriteria) {
        StringJoiner queryParams = new StringJoiner("&", "?", "");

        if (filterCriteria.name() != null && !filterCriteria.name().isEmpty()) {
            queryParams.add("name=" + filterCriteria.name());
        }
        if (filterCriteria.country() != null) {
            queryParams.add("country=" + filterCriteria.country());
        }

        queryParams.add("before=" + ServletUtil.formatInstantAsLocalDateTime(filterCriteria.before()));

        return queryParams.toString();
    }
}
