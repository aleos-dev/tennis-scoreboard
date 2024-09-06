package com.aleos.servlet;

import com.aleos.service.ImageService;
import com.aleos.configuration.AppContextAttribute;
import com.aleos.exception.ImageServiceException;
import com.aleos.model.dto.in.PlayerNamePayload;
import com.aleos.servicelocator.ServiceLocator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/avatars/*")
public class AvatarServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AvatarServlet.class.getName());

    private transient ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServiceLocator locator = (ServiceLocator) config.getServletContext()
                .getAttribute(AppContextAttribute.BEAN_FACTORY.toString());
        imageService = (ImageService) locator.getBean("imageService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var playerNamePayload = (PlayerNamePayload) req.getAttribute("playerNamePayload");

        var inputStreamOpt = imageService.getAvatarImageInputStream(playerNamePayload.name());

        if (inputStreamOpt.isPresent()) {
            try (InputStream inputStream = inputStreamOpt.get();
                 OutputStream outputStream = resp.getOutputStream()) {

                resp.setContentType("image/webp");

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw new ImageServiceException("Error processing the avatar with name: %s".formatted(playerNamePayload.name()), e);
            }
        } else {
            resp.setStatus(404);
        }
    }
}
