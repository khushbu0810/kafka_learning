package com.example.ProductsMicroservice.service.impl;

import com.example.ProductsMicroservice.model.Product;
import com.example.ProductsMicroservice.repository.ProductRepo;
import com.example.ProductsMicroservice.service.ProductCreatedEvent;
import com.example.ProductsMicroservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepo productRepo;
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger log= LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.productRepo = productRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Product createProduct(Product product) throws Exception{
        String productId = UUID.randomUUID().toString();
        Product createdProduct = productRepo.save(product);
        //publishing event
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                product.getTitle(),
                product.getPrice(),
                product.getQuantity());
        //for publishing thi event to kafka topic we use kafka Client : KafkaTemplate
        //topic , key , event

        /*
        Async behaviour without any acknowledgement
                kafkaTemplate.send("product-created-events-topic",productId,productCreatedEvent);
        but if you want a success response after completion use Completable Future

        -> to handle completed response use whenComplete method

        -> for sync --> use future.join() in end
         */


        /* ASYNC BEHAVIOUR
        CompletableFuture<SendResult<String,ProductCreatedEvent>> future =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
        future.whenComplete((result,exception)->{
            if(exception!=null){
                log.error("Failed to send message{}", exception.getMessage());
            }
            log.info("Message sent successfully{}", result.getRecordMetadata());
        });
        //making code synchronous
        future.join();
        */


        //SYNC BEHAVIOUR
        // use get method on kafka send function, this get method throws Exception

        log.info("Before publishing event");
        SendResult<String,ProductCreatedEvent> result =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();

        log.info("Partition: "+result.getRecordMetadata().partition());
        log.info("Topic: "+result.getRecordMetadata().topic());
        log.info("OffSet: "+result.getRecordMetadata().offset()); //index basically in that partition

        log.info("************* Returning Product object");
        return createdProduct;
    }
}
