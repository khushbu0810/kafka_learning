package com.example.ProductMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreationResponseDTO {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
