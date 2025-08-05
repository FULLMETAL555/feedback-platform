package com.feedback.feedback_service.security;

import com.feedback.feedback_service.service.ClientUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/signin",
                                "/auth/signup",
                                "/api/public/**",
                                "/error",
                                // Use paths WITHOUT /api prefix (as Spring Security sees them)
                                "/clients/register",
                                "/clients/login",
                                "/clients/refresh",
                                // Swagger UI resources (both with and without /api)
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/v3/api-docs",
                                // Also include /api versions for safety
                                "/api/clients/register",
                                "/api/clients/login",
                                "/api/clients/refresh",
                                "/api/swagger-ui/**",
                                "/api/swagger-ui.html",
                                "/api/v3/api-docs/**",
                                // Add analysis service endpoints
                                "/analysis/**",
                                "/api/analysis/**",
                                "/products/addproducts/",
                                "/feedback/submit/").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                // JWT filter first, will skip if no JWT token in header
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // API key filter next, will skip if Authorization Bearer header exists
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);




        // If you use API Key Auth, uncomment and configure this:
        // .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);



//        http.
//                csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()  // Allow all requests
//                );
//        // Removed the filter completely
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
