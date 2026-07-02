package com.example.OrderMicroservice.handler;

import com.example.OrderMicroservice.service.OrderService;
import com.example.core.commands.ApprovedOrderCommand;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${orders.commands.topic.name}")
public class OrderCommandsHandler {
    private final OrderService orderService;

    public OrderCommandsHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaHandler
    public void handleCommand(@Payload ApprovedOrderCommand command){
        orderService.approveOrder(command.getOrderId());

    }
}
