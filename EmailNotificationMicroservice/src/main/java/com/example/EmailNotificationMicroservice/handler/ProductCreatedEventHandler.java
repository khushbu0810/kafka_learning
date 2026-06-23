package com.example.EmailNotificationMicroservice.handler;

import com.example.EmailNotificationMicroservice.exception.NotRetryableException;
import com.example.EmailNotificationMicroservice.exception.RetryableException;
import com.example.core.event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@KafkaListener(topics = "product-created-events-topic")
/*
    this method should be invoked whenever a new message is received from specified kafka topic
    kafkaListener(topic1,topic2....)
    */
public class ProductCreatedEventHandler {
    private RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        /*
        if(true){
            throw new NotRetryableException("An error took place. No need to consume this message again. Sending to Dead Letter Topic.");
        }
         */
        log.info("Received new event: " + productCreatedEvent.getTitle());

        //Configuring external microservice
        String requestUrl = "http://localhost:8082";
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                log.info("Received response from a remote service running on 8082 Port" + response.getBody());
            }
        } catch (ResourceAccessException exp) {
            log.error(exp.getMessage());
            throw new RetryableException(exp);
        } catch (HttpServerErrorException exp){
            log.error(exp.getMessage());
            throw new NotRetryableException(exp);
        } catch (Exception exp){
            log.error(exp.getMessage());
            throw new NotRetryableException(exp);
        }
    }
}
