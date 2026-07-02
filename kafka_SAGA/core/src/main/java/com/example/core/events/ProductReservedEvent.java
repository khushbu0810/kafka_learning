package com.example.core.events;

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
public class ProductReservedEvent {
    private UUID orderId;
    private UUID productId;
    private BigDecimal productPrice;
    private Integer productQuantity;

    public ProductReservedEvent(UUID productId, BigDecimal price, Integer productQuantity) {
        this.productId=productId;
        this.productPrice=price;
        this.productQuantity=productQuantity;
    }
}
