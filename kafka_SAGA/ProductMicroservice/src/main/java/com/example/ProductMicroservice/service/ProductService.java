package com.example.ProductMicroservice.service;

import com.example.core.dto.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product reserve(Product desiredProduct, UUID orderId);

    void cancelReservation(Product productToCancel, UUID orderId);

    Product save(Product product);

    List<Product> findAll();
}
