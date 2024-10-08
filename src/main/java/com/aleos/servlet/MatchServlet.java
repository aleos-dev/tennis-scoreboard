package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.MatchUuidPayload;
import com.aleos.model.dto.out.MatchDto;
import com.aleos.model.enums.MatchStatus;
import com.aleos.service.MatchService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

@WebServlet("/matches/*")
public class MatchServlet extends HttpServlet {

    private transient MatchService matchService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        matchService = (MatchService) locator.getBean("matchService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var uuidPayload = req.getAttribute("matchUuidPayload");

        Optional<MatchDto> matchDtoOptional = matchService.findById((MatchUuidPayload) uuidPayload);

        matchDtoOptional.ifPresentOrElse(
                match -> {
                    req.setAttribute("match", match);
                    ServletUtil.forwardToJsp(req, resp, match.getStatus() == MatchStatus.ONGOING
                            ? "control/live-match"
                            : "control/completed-match");
                }, () -> {
                    ServletUtil.setErrors(req, "No match found");
                    ServletUtil.forwardToJsp(req, resp, "control/matches");
                }

        );
    }
}
