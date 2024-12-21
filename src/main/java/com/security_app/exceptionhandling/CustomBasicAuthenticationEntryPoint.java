package com.security_app.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setHeader("moAymanHeader", "Authentication failed my custom value !");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        // Get the current timestamp in ISO-8601 format
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        String message =  (authException != null && authException.getMessage() != null)? authException.getMessage() : "My hard Message Unathorized!";
                
        // Create a JSON response with a custom message and timestamp
        String jsonResponse = "{"
                + "\"status\": \"" + HttpStatus.UNAUTHORIZED.value() + "\","
                + "\"error\": \"" + HttpStatus.UNAUTHORIZED.getReasonPhrase() + "\","
                + "\"message\": \"Authentication failed: " + message + "\","
                + "\"path\": \"" + request.getRequestURI() + "\","
                + "\"timestamp\": \"" + timestamp + "\""
                + "}";

        // Write the JSON response to the response output stream
        response.getWriter().write(jsonResponse);
    }


}
