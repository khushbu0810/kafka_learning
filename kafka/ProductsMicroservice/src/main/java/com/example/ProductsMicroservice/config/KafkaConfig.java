package com.example.ProductsMicroservice.config;

import com.example.core.event.ProductCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    //this property will be read from application.properties file
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String deliveryTimeout;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    private String linger;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private String requestTimeout;

//    @Value("spring.kafka.producer.properties.enable.idempotence")
//    private String idempotence;

//    @Value("spring.kafka.producer.properties.max.in.flight.requests.per.connection")
//    private String inFlightRequests;

    //producer config
    Map<String,Object> producerConfigs(){
        Map<String,Object> prodConfig=new HashMap<>();
        prodConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        prodConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,keySerializer);
        prodConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,valueSerializer);
        prodConfig.put(ProducerConfig.ACKS_CONFIG,acks);
        prodConfig.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,deliveryTimeout);
        prodConfig.put(ProducerConfig.LINGER_MS_CONFIG,linger);
        prodConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,requestTimeout);
//        prodConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,idempotence);
//        prodConfig.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,inFlightRequests);
//        prodConfig.put(ProducerConfig.RETRIES_CONFIG,Integer.MAX_VALUE);

        return prodConfig;
    }

    //producer factory method using KafkaProducerFactor that create kafka producer instances in springBoot
    // factory is used to create objects
    @Bean
    ProducerFactory<String, ProductCreatedEvent> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    //kafka template object is used to send messages to kafka topics
    @Bean
    KafkaTemplate<String,ProductCreatedEvent> kafkaTemplate(){
        return new KafkaTemplate<String,ProductCreatedEvent>(producerFactory());
    }

    //creating new topic using topicBuilder class
    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name("product-created-events-topic")
                .partitions(3) //3 microservices will consume message from this topic
                .replicas(3) //copies stored on different broker //3 broker running in kafka
                .configs(Map.of("min.insync.replicas", "2")) //minimum 2 replicas must be successful
                .build();
    }
}
