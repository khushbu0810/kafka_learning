package com.example.OrderMicroservice.saga;

import com.example.core.commands.ReserveProductCommand;
import com.example.core.events.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {"${orders.events.topic.name}"})
public class OrderSaga {

    KafkaTemplate<String,Object>kafkaTemplate;
    private final String productsCommandsTopicName;

    public OrderSaga(KafkaTemplate<String,Object> kafkaTemplate,@Value("${products.commands.topic.name}") String productsCommandsTopicName){
        this.kafkaTemplate=kafkaTemplate;
        this.productsCommandsTopicName=productsCommandsTopicName;
    }

    /*
   1. we created this reserve product cmd in core package, now SAGA publish this cmd
   2. send this cmd as kafka message -> using kafka template
    */
    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event){
        ReserveProductCommand command=new ReserveProductCommand(
                event.getProductId(),
                event.getProductQuantity(),
                event.getOrderId()
        );
        kafkaTemplate.send(productsCommandsTopicName,command);
    }
}
