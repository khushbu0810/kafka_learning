package com.example.OrderMicroservice.service.impl;

import com.example.OrderMicroservice.model.OrderEntity;
import com.example.OrderMicroservice.repository.OrderRepository;
import com.example.OrderMicroservice.service.OrderService;
import com.example.core.dto.Order;
import com.example.core.types.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order placeOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerId(order.getCustomerId());
        entity.setStatus(OrderStatus.CREATED);
        entity.setProductId(order.getProductId());
        entity.setProductQuantity(order.getProductQuantity());
        orderRepository.save(entity);
        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getProductId(),
                entity.getProductQuantity(),
                entity.getStatus()
        );
    }
}
