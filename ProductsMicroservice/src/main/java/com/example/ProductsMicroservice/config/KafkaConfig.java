package com.example.ProductsMicroservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfig {
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
