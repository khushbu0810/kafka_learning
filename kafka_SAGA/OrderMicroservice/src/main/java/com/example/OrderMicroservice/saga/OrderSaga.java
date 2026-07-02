package com.example.OrderMicroservice.saga;

import com.example.core.events.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {"${orders.events.topic.name}"})
public class OrderSaga {

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event){

    }
}
