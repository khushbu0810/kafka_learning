package com.example.OrderMicroservice.service.impl;

import com.example.OrderMicroservice.model.OrderEntity;
import com.example.OrderMicroservice.repository.OrderRepository;
import com.example.OrderMicroservice.service.OrderService;
import com.example.core.dto.Order;
import com.example.core.events.OrderCreatedEvent;
import com.example.core.types.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private String ordersEventsTopicName;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            KafkaTemplate kafkaTemplate,
            @Value("${orders.events.topic.name}") String ordersEventsTopicName) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate=kafkaTemplate;
        this.ordersEventsTopicName=ordersEventsTopicName;
    }

    @Override
    public Order placeOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerId(order.getCustomerId());
        entity.setStatus(OrderStatus.CREATED);
        entity.setProductId(order.getProductId());
        entity.setProductQuantity(order.getProductQuantity());
        orderRepository.save(entity);

        //publishing order created event
        //1. creating instance of event first --> then kafka template send this message to kafka topic
        OrderCreatedEvent placeOrder = new OrderCreatedEvent(
                entity.getId(),
                entity.getCustomerId(),
                entity.getProductId(),
                entity.getProductQuantity()
        );
        //2.publish this message to kafka topic --> this topic creation must exixt in kafka config
        kafkaTemplate.send(ordersEventsTopicName,placeOrder);

        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getProductId(),
                entity.getProductQuantity(),
                entity.getStatus()
        );
    }
}
