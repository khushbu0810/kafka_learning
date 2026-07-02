package com.example.PaymentMicroservice.handler;

import com.example.core.commands.ProcessPaymentCommand;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics =  "${payments.commands.topic.name}")
public class PaymentCommandsHandler {

    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {

    }
}
