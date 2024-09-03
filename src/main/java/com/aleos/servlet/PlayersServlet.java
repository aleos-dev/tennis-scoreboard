package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.exception.UniqueConstraintViolationException;
import com.aleos.model.dto.in.PageableInfo;
import com.aleos.model.dto.in.PlayerFilterCriteria;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.service.PlayerService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        playerService = (PlayerService) locator.getBean("playerService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (ServletUtil.checkErrors(req, resp, "control/players")) {
            return;
        }

        var pageable = (PageableInfo) req.getAttribute("pageablePayload");
        var filterCriteria = (PlayerFilterCriteria) req.getAttribute("playerFilterCriteria");

        req.setAttribute("playersDto", playerService.findAll(pageable, filterCriteria));
        req.setAttribute("countryCodes", playerService.getCountryCodes());
        req.setAttribute("filterCriteria", filterCriteria);
        req.setAttribute("pageable", pageable);
        req.setAttribute("baseUrl", req.getContextPath() + req.getServletPath());

        // The query parameters (excluding pagination)
        req.setAttribute("queryParam", buildQueryString(filterCriteria));

        ServletUtil.forwardToJsp(req, resp, "control/players");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        PlayerPayload payload = (PlayerPayload) req.getAttribute("playerPayload");
        try {
            playerService.createPlayer(payload);

        } catch (UniqueConstraintViolationException e) {

            req.setAttribute("errorMessages", List.of("Player with name: %s already exists".formatted(payload.name())));
            ServletUtil.forwardToJsp(req, resp, "control/create-player");
        }
    }

    private String buildQueryString(PlayerFilterCriteria filterCriteria) {
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
