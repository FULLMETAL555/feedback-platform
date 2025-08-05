package com.feedback.feedback_service.controller;

import com.feedback.feedback_service.dto.SignInDTO;
import com.feedback.feedback_service.dto.SignUpDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.security.JwtUtil;
import com.feedback.feedback_service.security.TokenBlacklistService;
import com.feedback.feedback_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDTO signUpDto) {
        System.out.println("Signup endpoint hit!");
        try {
            authService.signUp(signUpDto); // Handles validation and password hashing
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Client registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInDTO signInDto) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password());
            var authenticatedUser = authenticationManager.authenticate(authToken);
            Client client = (Client) authenticatedUser.getPrincipal();

            String jwt = jwtUtil.generateToken(client.getClientId(), client.getEmail());
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "clientId", client.getClientId(),
                    "name", client.getName(),
                    "email", client.getEmail(),
                    "apikey",client.getApiKey()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Date expiryDate = jwtUtil.getExpiryDate(token);
            long expiryMillis = expiryDate != null ? expiryDate.getTime() : System.currentTimeMillis() + 3600000; // fallback: 1hr
            tokenBlacklistService.blackListToken(token, expiryMillis);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }


    // Optional: current user info endpoint
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        Client client = (Client) authentication.getPrincipal();
        return ResponseEntity.ok(Map.of(
                "clientId", client.getClientId(),
                "email", client.getEmail(),
                "name", client.getName()
        ));
    }
}
