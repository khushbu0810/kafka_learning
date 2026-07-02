package com.example.core.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReserveProductCommand {
    private UUID productId;
    private Integer productQuantity;
    private UUID orderId;
}
