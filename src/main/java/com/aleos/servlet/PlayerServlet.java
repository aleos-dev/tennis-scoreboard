package com.aleos.servlet;

import com.aleos.ImageService;
import com.aleos.configuration.AppContextAttribute;
import com.aleos.exception.ImageServiceException;
import com.aleos.exception.UniqueConstraintViolationException;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.model.dto.in.PlayerPayload;
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
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

@WebServlet("/players/*")
@MultipartConfig
public class PlayerServlet extends HttpServlet {

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
                () -> req.setAttribute("errorMessages", List.of("Player with name %s not exist".formatted(payload.name())))
        );

        ServletUtil.forwardToJsp(req, resp, CONTROL_PLAYER_JSP_REFERENCE);
    }


    private void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        var identifier = (PlayerNamePayload) req.getAttribute("playerNamePayload");
        var payload = (PlayerPayload) req.getAttribute("playerPayload");

        try {
            playerService.update(identifier, payload);

        } catch (UniqueConstraintViolationException e) {

            req.setAttribute("errorMessages", List.of("Player with name: %s already exists".formatted(payload.name())));
            req.setAttribute("editMode", true);
            req.setAttribute("playerDto", playerService.findByName(identifier).orElse(null));

            ServletUtil.forwardToJsp(req, resp, CONTROL_PLAYER_JSP_REFERENCE);
            return;
        }

        String oldName = identifier.name();
        String newName = payload.name();
        saveUploadedImage(extractImagePart(req), newName, oldName);

        ServletUtil.redirect(req, resp, "/players/" + payload.name());
    }

    private Part extractImagePart(HttpServletRequest req) {
        try {
            return req.getPart("image");
        } catch (IOException e) {
            throw new ImageServiceException("Can not retrieve the Part from request", e);
        } catch (ServletException e) {
            throw new ImageServiceException("Not multipart request type", e);
        }
    }

    private void saveUploadedImage(Part imagePart, String newName, String oldName) {
        try {
            String contentType = imagePart.getContentType();
            if (contentType.startsWith("image/")) {
                imageService.saveAvatarAsWebPImage(imagePart.getInputStream(), newName);
                imageService.remove(oldName);
            }

        } catch (IOException e) {
            throw new ImageServiceException("Can not retrieve an InputStream from the image Part", e);
        }
    }
}
