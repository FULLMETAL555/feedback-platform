package com.feedback.feedback_service.service;

import com.feedback.feedback_service.dto.SignUpDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientService clientService;

    public void signUp(SignUpDTO signUpDto) {
        if (clientRepository.existsByEmail(signUpDto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Client client = new Client();
        client.setName(signUpDto.name());
        client.setEmail(signUpDto.email());
        client.setPassword(passwordEncoder.encode(signUpDto.password()));
        String apiKey = clientService.generateAndStoreApiKey(client);
        System.out.println(apiKey);
        clientRepository.save(client);
        log.info("New client registered: {}", signUpDto.email());
    }
}
