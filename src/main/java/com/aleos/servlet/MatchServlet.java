package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.model.dto.in.MatchUuidPayload;
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

        matchService.findById((MatchUuidPayload) uuidPayload)
                .ifPresent(matchDto -> {
                    req.setAttribute("match", matchDto);
                    if (matchDto.getStatus() == MatchStatus.ONGOING) {
                        ServletUtil.forwardToJsp(req, resp, "control/live-match");
                        return;
                    }
                    ServletUtil.forwardToJsp(req, resp, "display/completed-match");
                });
    }
}
