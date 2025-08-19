package com.feedback.feedback_service.security;

import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.repository.ClientRepository;
import com.feedback.feedback_service.service.ClientService;
import com.feedback.feedback_service.util.HashUtil;
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

    private static final int KEY_EXPIRY_DAY = 30;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            // JWT is present—let JwtAuthFilter handle this request
            filterChain.doFilter(request, response);
            return;
        }

        // Get the full path for debugging
        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("=== ApiKeyAuthFilter Debug ===");
        System.out.println("Request Path: " + path);
        System.out.println("Request Method: " + method);
        System.out.println("Context Path: " + request.getContextPath());
        System.out.println("Servlet Path: " + request.getServletPath());

//        if ("OPTIONS".equals(method)) {
//            // Create dummy authentication principal just to bypass security
//            UsernamePasswordAuthenticationToken dummyAuth =
//                    new UsernamePasswordAuthenticationToken("anonymous", null, Collections.emptyList());
//            SecurityContextHolder.getContext().setAuthentication(dummyAuth);
//            filterChain.doFilter(request, response);
//            return;
//        }


        // Allow public endpoints to bypass the filter
        if (isPublicEndpoint(path) || "OPTIONS".equals(method) ) {
            System.out.println("✅ Public endpoint - bypassing filter");
            filterChain.doFilter(request, response);
            return;
        }
        // Check if this is the analysis service (you can use port or path logic)
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        System.out.println(serverName+" "+serverPort);

        String apiKey = request.getHeader("X-API-KEY");
        System.out.println("Request apikey: " + apiKey);

        if (apiKey == null || !clientService.isValidKey(apiKey) ) {
            System.out.println("❌ Invalid or missing API key");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: Invalid or missing API key\"}");
            return;
        }

        // Apply rate limit
        if (!rateLimiterService.isAllowed(apiKey) ) {
            System.out.println("❌ Rate limit exceeded");
            response.setStatus(429); // Use proper rate limit status code
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Rate limit exceeded. Try again later.\"}");
            return;
        }

        System.out.println("✅ API key valid - proceeding");
        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()->new RuntimeException("Invalid API key"));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(client, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path) {
        // More robust path matching
        return path.equals("/api/clients/register") ||
                path.equals("/api/clients/login") ||
                path.equals("/api/clients/refresh") ||
                path.startsWith("/api/v3/api-docs") ||
                path.startsWith("/api/swagger-ui") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                // Add analysis service endpoints
                path.startsWith("/api/analysis/") ||
                path.startsWith("/analysis/") ||
                path.startsWith("/api/auth/signin") ||
                path.startsWith("/api/auth/signup");
    }
}