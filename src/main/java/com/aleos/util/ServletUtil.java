package com.aleos.util;

import com.aleos.exception.JspForwardingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class ServletUtil {

    public static void forwardToJsp(HttpServletRequest req, HttpServletResponse resp, String jspName) {
        try {
            req.getRequestDispatcher(JspPathResolver.getPath("%s").formatted(jspName)).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new JspForwardingException("Error forwarding to the %s JSP".formatted(jspName), e);
        }
    }

    public static void redirect(HttpServletRequest req, HttpServletResponse resp, String path) {
        try {
            resp.sendRedirect(req.getContextPath() + path);
        } catch (IOException e) {
            throw new JspForwardingException("Error redirection to the %s ".formatted(path), e);
        }
    }

}
