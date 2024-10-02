package com.microservice.entertainment.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String message;
        if (authException instanceof BadCredentialsException) {
            message = "Invalid email or password.";
        } else {
            message = "You are not authenticated.";
        }
        response.getWriter().append(json(message, request.getRequestURI()));
    }

    private CharSequence json(String message, String path) {
        long date = new Date().getTime();
        return "{"
                + "\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Unauthorized\", "
                + "\"message\": \"" + message + "\", "
                + "\"path\": \"" + path + "\"}";
    }
}