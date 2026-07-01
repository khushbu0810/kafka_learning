package com.example.OrderMicroservice.service.impl;

import com.example.OrderMicroservice.dto.OrderHistoryRequest;
import com.example.OrderMicroservice.model.OrderHistoryEntity;
import com.example.OrderMicroservice.repository.OrderHistoryRepository;
import com.example.OrderMicroservice.service.OrderHistoryService;
import com.example.core.types.OrderStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    OrderHistoryRepository orderHistoryRepository;

    @Autowired
    public OrderHistoryServiceImpl(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Override
    public void add(UUID orderId, OrderStatus orderStatus) {
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setOrderId(orderId);
        orderHistoryEntity.setStatus(orderStatus);
        orderHistoryEntity.setCreatedAt(new Timestamp(new Date().getTime()));
        orderHistoryRepository.save(orderHistoryEntity);

    }

    @Override
    public List<OrderHistoryRequest> findByOrderId(UUID orderId) {
        var entities = orderHistoryRepository.findByOrderId(orderId);
        return entities.stream().map(entity -> {
            OrderHistoryRequest orderHistory = new OrderHistoryRequest();
            BeanUtils.copyProperties(entity, orderHistory);
            return orderHistory;
        }).toList();
    }
}
