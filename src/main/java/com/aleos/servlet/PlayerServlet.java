package com.aleos.servlet;

import com.aleos.configuration.AppContextAttribute;
import com.aleos.exception.ImageServiceException;
import com.aleos.exception.UniqueConstraintViolationException;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.model.dto.in.PlayerPayload;
import com.aleos.service.ImageService;
import com.aleos.service.PlayerService;
import com.aleos.servicelocator.ServiceLocator;
import com.aleos.util.ServletUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/players/*")
@MultipartConfig
public class PlayerServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(PlayerServlet.class.getName());

    private transient PlayerService playerService;

    private transient ImageService imageService;

    private static final String CONTROL_PLAYER_JSP_REFERENCE = "control/player";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        playerService = (PlayerService) locator.getBean("playerService");
        imageService = (ImageService) locator.getBean("imageService");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(req.getMethod()) && "PATCH".equalsIgnoreCase(req.getParameter("_method"))) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (ServletUtil.checkErrors(req, resp, CONTROL_PLAYER_JSP_REFERENCE)) {
            return;
        }

        var payload = (PlayerNamePayload) req.getAttribute("playerNamePayload");

        var playerDto = playerService.findByName(payload);
        playerDto.ifPresentOrElse(
                player -> req.setAttribute("playerDto", player),
                () -> ServletUtil.setErrors(req, "Player with name %s not exist".formatted(payload.name())));

        ServletUtil.forwardToJsp(req, resp, CONTROL_PLAYER_JSP_REFERENCE);
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        if (ServletUtil.checkErrors(req, resp, CONTROL_PLAYER_JSP_REFERENCE)) {
            return;
        }

        var identifier = (PlayerNamePayload) req.getAttribute("playerNamePayload");
        var payload = (PlayerPayload) req.getAttribute("playerPayload");

        // not atomic
        try {

            playerService.update(identifier, payload);
            imageService.saveOrUpdateAvatar(req, payload.name(), identifier.name());

            var urlEncodedName = URLEncoder.encode(payload.name(), StandardCharsets.UTF_8);
            ServletUtil.redirect(req, resp, "/players/" + urlEncodedName);

        } catch (UniqueConstraintViolationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            ServletUtil.setErrors(req, "Player with name: %s already exists".formatted(payload.name()));
            ServletUtil.forwardToJsp(req, resp, CONTROL_PLAYER_JSP_REFERENCE);

        } catch (ImageServiceException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            ServletUtil.setErrors(req, "The image can't be loaded");
            ServletUtil.forwardToJsp(req, resp, CONTROL_PLAYER_JSP_REFERENCE);
        }
    }
}
