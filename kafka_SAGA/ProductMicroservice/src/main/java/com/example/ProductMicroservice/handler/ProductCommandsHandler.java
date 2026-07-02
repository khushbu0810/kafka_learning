package com.example.ProductMicroservice.handler;

import com.example.ProductMicroservice.service.ProductService;
import com.example.core.commands.ReserveProductCommand;
import com.example.core.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${products.commands.topic.name}")
@Slf4j
public class ProductCommandsHandler {

    private final ProductService productService;

    public ProductCommandsHandler(ProductService productService) {
        this.productService = productService;
    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {
        try {
            Product desiredProduct = new Product(command.getProductId(), command.getProductQuantity());
            Product reservedProduct = productService.reserve(desiredProduct, command.getOrderId());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
