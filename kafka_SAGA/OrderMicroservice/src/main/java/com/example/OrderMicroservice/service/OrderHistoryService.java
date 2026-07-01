package com.example.OrderMicroservice.service;

import com.example.OrderMicroservice.dto.OrderHistoryRequest;
import com.example.core.types.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);
    List<OrderHistoryRequest>findByOrderId(UUID orderId);
}
