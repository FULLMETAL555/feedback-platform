package com.feedback.feedback_service.security;

import com.feedback.feedback_service.service.ClientService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final int KEY_EXPIRY_DAY =30;

    @Autowired
    private ClientService clientService;

    @Autowired
    private  RateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        // Allow public endpoints to bypass the filter
        String path = request.getRequestURI();
        if (path.startsWith("/api/clients/register") ||
                path.startsWith("/api/clients/login") ||
                path.startsWith("/api/clients/refresh") ||
                path.startsWith("/api/v3/api-docs") ||
                path.startsWith("/api/swagger-ui")) {
            System.out.println("Out");
            filterChain.doFilter(request, response);
            return;
        }
        String apiKey=request.getHeader("X-API-KEY");
        System.out.println("Request apikey : "+apiKey);

        if(apiKey==null || !clientService.isValidKey(apiKey)){
            System.out.println("IN");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid or missing API key");
            return;
        }


        // 🔒 Apply rate limit
        if (!rateLimiterService.isAllowed(apiKey)) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(apiKey, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
