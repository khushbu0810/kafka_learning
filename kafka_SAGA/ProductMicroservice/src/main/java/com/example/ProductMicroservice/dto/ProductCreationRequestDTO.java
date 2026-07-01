package com.example.ProductMicroservice.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreationRequestDTO {
    @NonNull
    private String name;
    @NonNull
    @Positive
    private BigDecimal price;
    @NonNull
    @Positive
    private Integer quantity;
}
