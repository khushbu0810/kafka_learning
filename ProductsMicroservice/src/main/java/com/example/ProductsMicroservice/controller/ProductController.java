package com.example.ProductsMicroservice.controller;

import com.example.ProductsMicroservice.model.Product;
import com.example.ProductsMicroservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;
    @Autowired
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        Product productCreated= productService.createProduct(product);
        Integer productId=productCreated.getProductId();
        return ResponseEntity.status(200).body("productId:"+productId);
    }
}
