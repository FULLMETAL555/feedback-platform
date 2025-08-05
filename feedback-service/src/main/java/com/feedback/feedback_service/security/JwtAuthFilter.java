package com.feedback.feedback_service.security;


import ch.qos.logback.core.util.StringUtil;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.repository.ClientRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter  {

    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String headerAuth = request.getHeader("Authorization");
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            System.out.println("AUTH FILTER");
            // Not a JWT request, skip and let other filters try
            filterChain.doFilter(request, response);
            return;
        }

        String jwt=parseJwt(request);

        if(jwt!=null){

            //check for token
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token has been revoked\"}");
                return;
            }

                //getting client by jwt
            try {
                var jwtClaims= jwtUtil.validateAndParse(jwt).getBody();
                String clientId= jwtClaims.getSubject();
                Client client= clientRepository.findByClientId(clientId).orElse(null);

                if(client!=null){
                    UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(
                            client,null,client.getAuthorities());
                    auth.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            }catch (JwtException ex){
                System.out.println("Exception :" + ex.getMessage());
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }


        return null;
    }
}
