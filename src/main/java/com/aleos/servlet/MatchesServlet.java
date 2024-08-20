package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.MatchFilterCriteria;
import com.aleos.model.dto.in.MatchPayload;
import com.aleos.model.dto.in.PageableInfo;
import com.aleos.model.dto.out.MatchesDto;
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
import java.util.UUID;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

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
        var pageable = (PageableInfo) req.getAttribute("pageablePayload");
        var filterCriteria = (MatchFilterCriteria) req.getAttribute("matchFilterCriteria");

        MatchesDto all = matchService.findAll(pageable, filterCriteria);

        makeResponse(resp, all);
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            var payload = (MatchPayload) request.getAttribute("matchPayload");

            UUID matchId = matchService.createMatch(payload);

            String asString = objectMapper.writeValueAsString(matchId);
            response.getWriter().write(asString);

    }
}
