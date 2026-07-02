package com.example.ProductMicroservice.handler;

import com.example.ProductMicroservice.service.ProductService;
import com.example.core.commands.ReserveProductCommand;
import com.example.core.dto.Product;
import com.example.core.events.ProductReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${products.commands.topic.name}")
@Slf4j
public class ProductCommandsHandler {

    private final ProductService productService;
    KafkaTemplate<String, Object> kafkaTemplate;
    private final String productsEventsTopicName;

    public ProductCommandsHandler(
            ProductService productService,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${products.events.topic.name}") String productsEventsTopicName) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
        this.productsEventsTopicName = productsEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {
        try {
            Product desiredProduct = new Product(command.getProductId(), command.getProductQuantity());
            Product reservedProduct = productService.reserve(desiredProduct, command.getOrderId());

            //after successful reserve product , publish event
            ProductReservedEvent productReservedEvent = new ProductReservedEvent(
                    command.getProductId(),
                    reservedProduct.getPrice(),
                    command.getProductQuantity()
            );
            //publish this event
            kafkaTemplate.send(productsEventsTopicName,productReservedEvent);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
