package com.feedback.feedback_service.service;

import com.feedback.feedback_service.dto.ProductRequestDTO;
import com.feedback.feedback_service.dto.ProductResponseDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Product;
import com.feedback.feedback_service.repository.ClientRepository;
import com.feedback.feedback_service.repository.ProductRepository;
import com.feedback.feedback_service.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private  ClientService clientService;
    public ProductResponseDTO addProduct(String apiKey, ProductRequestDTO dto){
        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()-> new RuntimeException("Invalid Apikey"));



        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setClient(client);

        boolean exists = productRepository.existsByNameAndClient(product.getName(), client);
        if (exists) {
            throw new RuntimeException("Product with this name already exists for this client.");
        }

        Product saved = productRepository.save(product);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(saved.getId());
        responseDTO.setName(saved.getName());
        responseDTO.setDescription(saved.getDescription());
        responseDTO.setClientId(saved.getClient().getId());

        return responseDTO;
    }

    public Product findById(long productID) {
        return productRepository.findById(productID).orElseThrow(()->new RuntimeException("This product does not exist"));
    }

    public List<ProductResponseDTO> getProductByApiKey(String apiKey){
        String hashApiKey= HashUtil.hashApiKey(apiKey);
        Client client=clientRepository.findByHashedApiKey(hashApiKey).orElseThrow(()->new RuntimeException("Invalid API key"));

        return client.getProducts().stream().map(product -> {
            ProductResponseDTO dto=new ProductResponseDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            return dto;
        }).toList();
    }
    public void deleteProduct(Product product){
        productRepository.delete(product);
    }
}
