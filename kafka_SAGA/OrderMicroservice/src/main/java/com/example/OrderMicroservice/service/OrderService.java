package com.example.OrderMicroservice.service;

import com.example.core.dto.Order;

public interface OrderService {
    Order placeOrder(Order order);
}
