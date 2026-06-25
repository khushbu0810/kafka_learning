package com.example.EmailNotificationMicroservice.handler;

import com.example.EmailNotificationMicroservice.exception.NotRetryableException;
import com.example.EmailNotificationMicroservice.exception.RetryableException;
import com.example.EmailNotificationMicroservice.model.ProcessedEventEntity;
import com.example.EmailNotificationMicroservice.repository.ProcessedEventRepo;
import com.example.core.event.ProductCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
//@KafkaListener(topics = "product-created-events-topic",groupId = "product-created-events")
@KafkaListener(topics = "product-created-events-topic", groupId = "product-created-events")
/*
    this method should be invoked whenever a new message is received from specified kafka topic
    kafkaListener(topic1,topic2....)
    */
public class ProductCreatedEventHandler {
    private RestTemplate restTemplate;
    private ProcessedEventRepo processedEventRepo;

    public ProductCreatedEventHandler(RestTemplate restTemplate, ProcessedEventRepo processedEventRepo) {
        this.restTemplate = restTemplate;
        this.processedEventRepo = processedEventRepo;
    }

    //transactional --> if this method throws any exception then any changes that are done in DB will be rolled back
    @Transactional
    @KafkaHandler
    public void handle(@Payload ProductCreatedEvent productCreatedEvent,
                       @Header(value = "uniqueMessageId") String messageId,
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        /*
        -> for reading unique messageId from headers, adding another parameter headers to method as messageId and messageKey

        if(true){
            throw new NotRetryableException("An error took place. No need to consume this message again. Sending to Dead Letter Topic.");
        }
         */
        log.info("Received new event: " + productCreatedEvent.getTitle() + " with productId: " + productCreatedEvent.getProductId());

        //checking this message is processed earlier already or not
        ProcessedEventEntity existingRecord= processedEventRepo.findByMessageId(messageId);
        if(existingRecord!=null){
            log.info("Found a duplicate message id: {}",existingRecord.getMessageId());
            return;
        }

        //Configuring external microservice
        //if ye service down hai then exception will be thrown
        String requestUrl = "http://localhost:8082/response/200";
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                log.info("Received response from a remote service running on 8082 Port" + response.getBody());
            }
        } catch (ResourceAccessException exp) {
            log.error(exp.getMessage());
            throw new RetryableException(exp);
        } catch (HttpServerErrorException exp) {
            log.error(exp.getMessage());
            throw new NotRetryableException(exp);
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new NotRetryableException(exp);
        }

        //saving unique message id to DB
        try {
            processedEventRepo.save(new ProcessedEventEntity(messageId, productCreatedEvent.getProductId()));
        } catch (DataIntegrityViolationException exp){
            throw new NotRetryableException(exp);
        }
    }

}
