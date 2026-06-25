package com.example.ProductsMicroservice.controller;

import com.example.ProductsMicroservice.exception.KafkaErrorMessage;
import com.example.ProductsMicroservice.model.Product;
import com.example.ProductsMicroservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {
    ProductService productService;
    @Autowired
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product productCreated = productService.createProduct(product);
            Integer productId = productCreated.getProductId();
            return ResponseEntity.status(200).body("Product Created with product-Id:" + productId);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseEntity.status(500).body(new KafkaErrorMessage(new Date(),e.getMessage(),"/products"));
        }
    }
}
