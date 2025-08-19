package com.feedback.feedback_service.security;

import com.feedback.feedback_service.service.ClientUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientUserDetailsService userDetailsService;
    // If you have an API Key filter, inject it here:
    // private final ApiKeyAuthFilter apiKeyAuthFilter;

    private  final JwtAuthFilter jwtAuthFilter;
    private final ApiKeyAuthFilter apiKeyAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless APIs
                .csrf(csrf -> csrf.disable())

                // Use stateless session management (no sessions)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configure URL authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/signin",
                                "/auth/signup",
                                "/api/public/**",
                                "/error",
                                "/clients/register",
                                "/clients/login",
                                "/clients/refresh",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/v3/api-docs",
                                "/api/clients/register",
                                "/api/clients/login",
                                "/api/clients/refresh",
                                "/api/swagger-ui/**",
                                "/api/swagger-ui.html",
                                "/api/v3/api-docs/**",
                                "/analysis/**",
                                "/api/analysis/**",
                                "/products/addproducts/",
                                "/feedback/submit/"
                        ).permitAll()

                        // Allow all OPTIONS requests without authentication
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Register your authentication provider
                .authenticationProvider(authenticationProvider())

                // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Add API key authentication filter also before UsernamePasswordAuthenticationFilter
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Uses your custom user details service and BCrypt for secure password authentication.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * BCrypt is recommended for password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager used for manual authentication (e.g. in controllers).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
