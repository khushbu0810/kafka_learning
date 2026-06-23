package com.example.EmailNotificationMicroservice.handler;

import com.example.EmailNotificationMicroservice.exception.NotRetryableException;
import com.example.core.event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "product-created-events-topic")
/*
    this method should be invoked whenever a new message is received from specified kafka topic
    kafkaListener(topic1,topic2....)
    */
public class ProductCreatedEventHandler {

    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        if(true){
            throw new NotRetryableException("An error took place. No need to consume this message again. Sending to Dead Letter Topic.");
        }
        log.info("Received new event: "+ productCreatedEvent.getTitle());

    }
}
