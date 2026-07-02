package com.example.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreatedEvent {
    private UUID orderId;
    private UUID customerId;
    private UUID productId;
    private Integer productQuantity;
}
