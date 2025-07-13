package com.feedback.feedback_service.controller;

import com.feedback.feedback_service.dto.ClientRegistrationDTO;
import com.feedback.feedback_service.dto.ClientRegistrationResponse;
import com.feedback.feedback_service.dto.ClientResponseDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/me")
    public ResponseEntity<Client> getClientInfo(@RequestHeader("X-API-KEY") String apiKey) {
        Client client = clientService.getClientByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API key"));
        return ResponseEntity.ok(client);
    }


    @PostMapping("/register")
    public ResponseEntity<ClientRegistrationResponse> registerClient(@Valid @RequestBody ClientRegistrationDTO dto){
        Client client=new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        String apiKey = clientService.generateAndStoreApiKey(client);
        System.out.println(apiKey);
        Client rclient= clientService.registerClient(client);

        ClientRegistrationResponse response = new ClientRegistrationResponse(
                rclient.getApiKey(),
                rclient.getClientId(),
                rclient.getEmail(),
                rclient.getName()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login")
    public ClientResponseDTO login(@RequestParam String email) {
        return clientService.getClientInfoByEmail(email);
    }

    @GetMapping("/verify")
    public boolean verifyApiKey(@RequestHeader("X-API-KEY") String apiKey){
        return clientService.getClientByApiKey(apiKey).isPresent();
    }

    @PutMapping("/regenerate-key")
    public ResponseEntity<String> regenerateKey(@RequestHeader("X-API_KEY") String apiKey){
        Client updatedClient=clientService.regenerateApiKey(apiKey);

        return ResponseEntity.ok("New api key : "+updatedClient.getApiKey());
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshApiKey(@RequestParam Long clientId) {
        String newKey = clientService.refreshApiKey(clientId);
        return ResponseEntity.ok("Your new API Key: " + newKey);
    }
}
