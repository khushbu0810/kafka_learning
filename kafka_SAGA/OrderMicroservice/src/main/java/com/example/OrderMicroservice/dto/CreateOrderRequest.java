package com.example.OrderMicroservice.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {
    @NonNull
    private UUID customerId;
    @NonNull
    private UUID productId;
    @NonNull
    @Positive
    private Integer productQuantity;
}
