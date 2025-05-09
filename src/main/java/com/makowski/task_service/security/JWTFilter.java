package com.makowski.task_service.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JWTFilter implements Filter {

    private final AuthService authService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String username = authService.validateTokenGetUsername(authHeader).block();
                httpRequest.setAttribute("username", username);
            } catch (Exception e) {
                httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid Authorization header");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}