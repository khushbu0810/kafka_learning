package com.example.coreModule.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepositRequestedEvent {
    private String senderId;
    private String recipientId;
    private BigDecimal amount;
}
