package com.example.core.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import jakarta.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditCardProcessorRequest {

    @NonNull
    @Positive
    private BigInteger creditCardNumber;

    @NonNull
    @Positive
    private BigDecimal paymentAmount;
}
