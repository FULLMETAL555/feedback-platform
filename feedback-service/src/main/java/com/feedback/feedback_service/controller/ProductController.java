package com.feedback.feedback_service.controller;

import com.feedback.feedback_service.dto.ProductRequestDTO;
import com.feedback.feedback_service.dto.ProductResponseDTO;
import com.feedback.feedback_service.model.Client;
import com.feedback.feedback_service.model.Product;
import com.feedback.feedback_service.repository.ClientRepository;
import com.feedback.feedback_service.service.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping("/addproducts")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestHeader("X-API-KEY") String apiKey,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.addProduct(apiKey, productRequestDTO));
    }

    @GetMapping("/getallproducts")
    public  ResponseEntity<List<ProductResponseDTO>> geAllProductsForClient(@RequestHeader("X-API-KEY") String apiKey){
        return ResponseEntity.ok(productService.getProductByApiKey(apiKey));
    }

    @DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<String> deleteProduct(@RequestHeader("X-API-KEY") String apiKey,@PathVariable Long productId){
        Client client=clientRepository.findByApiKey(apiKey).orElseThrow(()->new RuntimeException("INVALID API"));
        Product product=productService.findById(productId);
        if(!product.getClient().getClientId().equals(client.getClientId())){
            throw new RuntimeException("PRODUCT DOES NOT MATCH THE CLIENT");
        }
        productService.deleteProduct(product);
        return ResponseEntity.ok("Product DELETED");

    }

    @GetMapping("/")
}
