package com.example.WithdrawlMicroservice.handler;

import com.example.coreModule.event.WithdrawalRequestedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "withdraw-money-topic",containerFactory = "kafkaListenerContainerFactory")
@Slf4j
public class WithdrawalRequestedEventHandler {
    @KafkaHandler
    public void handle(@Payload WithdrawalRequestedEvent withdrawalRequestedEvent){
        log.info("Received a new withdrawal event: {} ",withdrawalRequestedEvent.getAmount());
    }
}
