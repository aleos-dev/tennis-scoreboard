package com.aleos.util;

import com.aleos.exception.JspForwardingException;
import com.aleos.exception.ServletForwardingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class ServletUtil {

    public static void forward(HttpServletRequest req, HttpServletResponse resp, String servletContext) {
        try {
            req.getRequestDispatcher(servletContext).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ServletForwardingException("Error forwarding to the servlet %s".formatted(servletContext), e);
        }
    }

    public static void forwardToJsp(HttpServletRequest req, HttpServletResponse resp, String jspName) {
        try {
            req.getRequestDispatcher(JspPathResolver.getPath("%s").formatted(jspName)).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new JspForwardingException("Error forwarding to the %s JSP".formatted(jspName), e);
        }
    }

    public static void redirect(HttpServletRequest req, HttpServletResponse resp, String path) {
        try {
            resp.sendRedirect(req.getContextPath() + (path.startsWith("/") ? path : "/" + path));
        } catch (IOException e) {
            throw new JspForwardingException("Error redirection to the %s ".formatted(path), e);
        }
    }

    public static String formatInstantAsLocalDateTime(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    public static boolean checkErrors(HttpServletRequest req, HttpServletResponse resp, String jspName) {
        if (req.getAttribute("errorMessages") != null) {
            ServletUtil.forwardToJsp(req, resp, jspName);
            return true;
        }
        return false;
    }
}
