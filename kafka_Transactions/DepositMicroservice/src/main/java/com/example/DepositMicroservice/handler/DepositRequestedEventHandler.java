package com.example.DepositMicroservice.handler;

import com.example.coreModule.event.DepositRequestedEvent;
import com.example.coreModule.event.WithdrawalRequestedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "withdraw-money-topic",containerFactory = "kafkaListenerContainerFactory")
@Slf4j
public class DepositRequestedEventHandler {
    @KafkaHandler
    public void handle(@Payload DepositRequestedEvent depositRequestedEvent){
        log.info("Received a new withdrawal event: {} ",depositRequestedEvent.getAmount());
    }
}
