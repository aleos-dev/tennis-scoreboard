package com.aleos.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspPathResolver {

    public static String getPath(String jspFileName) {
        String pathTemplate = PropertiesUtil.get("jsp.url.path.template").orElse("/WEB-INF/jsp/%s.jsp");
        return String.format(pathTemplate, jspFileName);
    }
}
