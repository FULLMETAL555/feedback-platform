package com.feedback.feedback_service.service;

import com.feedback.feedback_service.dto.ClientResponseDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.repository.ClientRepository;
import com.feedback.feedback_service.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private static final SecureRandom secureRandom=new SecureRandom();


    public Client registerClient(Client client){
        String rawKey= UUID.randomUUID().toString();

        return clientRepository.save(client);
    }

    public Optional<Client> getClientByApiKey(String apiKey){
        return clientRepository.findByApiKey(apiKey);
    }
    public ClientResponseDTO getClientInfoByEmail(String email){
        Client client = clientRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Client not found with email" + email));
        return mapToResponseDTO(client);
    }

    private ClientResponseDTO mapToResponseDTO(Client client){
        ClientResponseDTO dto= new ClientResponseDTO();

        dto.setClientId(client.getClientId());
        dto.setName(client.getName());
        dto.setApiKey(client.getApiKey());
        dto.setEmail(client.getEmail());
        return dto;
    }

    public Client regenerateApiKey(String oldApiKey){
        Client client=clientRepository.findByApiKey(oldApiKey).orElseThrow(()->new RuntimeException("INVALID API"));

        String newKey= UUID.randomUUID().toString().replace("-","") + UUID.randomUUID().toString().substring(0,8);
        client.setApiKey(newKey);

        return clientRepository.save(client);
    }


    public String generateAndStoreApiKey(Client client){
        byte[] tokenBytes =new byte[32];
        secureRandom.nextBytes(tokenBytes);
        String rawKey= Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        String hashedKey = HashUtil.hashApiKey(rawKey);
        client.setApiKey(rawKey);
        client.setHashedApiKey(hashedKey);
        client.setApiKeyCreatedAt(LocalDateTime.now());
        clientRepository.save(client);
        return rawKey;
    }

    public boolean isValidKey(String providedApiKey){
        String hashedKey = HashUtil.hashApiKey(providedApiKey);
        System.out.println("PROVIDED RAW KEY: " + providedApiKey);
        System.out.println("HASHED (runtime): " + hashedKey);
        return clientRepository.findByHashedApiKey(hashedKey.trim()).isPresent();
    }

    public String refreshApiKey(Long clientId){
        Client client= clientRepository.findById(clientId).orElseThrow();
        return generateAndStoreApiKey(client);
    }

    public boolean isExpired(String rawApiKey){
        String hashedKey= HashUtil.hashApiKey(rawApiKey);
        Optional<Client> clientOpt=clientRepository.findByHashedApiKey(hashedKey);

        if(clientOpt.isPresent()){
            LocalDateTime createdAt=clientOpt.get().getCreatedAt();
            return createdAt.plusDays(30).isBefore(LocalDateTime.now());
        }
        return true;

    }

    public Client getClientByConvertingHashed(String apiKey) {
        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()-> new RuntimeException("Invalid Apikey"));

    return client;
    }
}
