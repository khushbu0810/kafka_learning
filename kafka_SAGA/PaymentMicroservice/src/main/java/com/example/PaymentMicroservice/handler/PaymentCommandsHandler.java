package com.example.PaymentMicroservice.handler;

import com.example.PaymentMicroservice.service.PaymentService;
import com.example.core.commands.ProcessPaymentCommand;
import com.example.core.dto.Payment;
import com.example.core.events.PaymentProcessEvent;
import com.example.core.events.PaymentProcessFailedEvent;
import com.example.core.exception.CreditCardProcessorUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${payments.commands.topic.name}")
@Slf4j
public class PaymentCommandsHandler {
    private PaymentService paymentService;
    KafkaTemplate<String, Object> kafkaTemplate;
    private final String paymentsEventsTopicName;

    public PaymentCommandsHandler(
            PaymentService paymentService,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${payments.events.topic.name}") String paymentsEventsTopicName) {
        this.paymentService = paymentService;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentsEventsTopicName = paymentsEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {
        try {
            Payment payment = new Payment(
                    command.getOrderId(),
                    command.getProductId(),
                    command.getProductPrice(),
                    command.getProductQuantity()
            );
            Payment processedPayment = paymentService.process(payment);
            PaymentProcessEvent paymentProcessEvent = new PaymentProcessEvent(processedPayment.getOrderId(), processedPayment.getId());
            //publish this event
            kafkaTemplate.send(paymentsEventsTopicName, paymentProcessEvent);
        } catch (CreditCardProcessorUnavailableException e) {
            PaymentProcessFailedEvent paymentProcessFailedEvent = new PaymentProcessFailedEvent(
                    command.getOrderId(),
                    command.getProductId(),
                    command.getProductQuantity()
            );
            kafkaTemplate.send(paymentsEventsTopicName, paymentProcessFailedEvent);
        }
    }
}
