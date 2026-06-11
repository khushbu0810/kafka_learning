package com.example.ProductsMicroservice.repository;

import com.example.ProductsMicroservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
}
