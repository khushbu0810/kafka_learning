package com.example.PaymentMicroservice.service;

import com.example.core.dto.Payment;

import java.util.List;

public interface PaymentService {
    Payment process(Payment payment);

    List<Payment> findAll();
}
