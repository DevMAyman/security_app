package com.security_app.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 Forbidden
        response.setContentType("application/json;charset=UTF-8");

        // Get the current timestamp in ISO-8601 format
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        // Create a custom JSON response
        String jsonResponse = "{"
                + "\"status\": \"" + HttpServletResponse.SC_FORBIDDEN + "\","
                + "\"error\": \"Forbidden\","
                + "\"message\": \"" + accessDeniedException.getMessage() + "\","
                + "\"path\": \"" + request.getRequestURI() + "\","
                + "\"timestamp\": \"" + timestamp + "\""
                + "}";

        // Write the JSON response to the response output stream
        response.getWriter().write(jsonResponse);
    }
}
