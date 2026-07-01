package com.example.ProductMicroservice.controller;

import com.example.ProductMicroservice.dto.ProductCreationRequestDTO;
import com.example.ProductMicroservice.dto.ProductCreationResponseDTO;
import com.example.ProductMicroservice.service.ProductService;
import com.example.core.dto.Product;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAll() {
        return productService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreationResponseDTO save(@RequestBody @Valid ProductCreationRequestDTO request) {
        var product = new Product();
        BeanUtils.copyProperties(request, product);
        Product result = productService.save(product);

        var productCreationResponse = new ProductCreationResponseDTO();
        BeanUtils.copyProperties(result, productCreationResponse);
        return productCreationResponse;
    }
}
