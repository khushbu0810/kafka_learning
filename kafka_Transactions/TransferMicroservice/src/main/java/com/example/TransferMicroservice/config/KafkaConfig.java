package com.example.TransferMicroservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${withdraw-money-topic}")
    private String withdrawTopicName;

    @Value("${deposit-money-topic}")
    private String depositTopicName;

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

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private String idempotence;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private String inFlightRequests;

    @Value("${spring.kafka.producer.transaction-id-prefix}")
    private String transactionIdPrefix; //assigning unique transaction ID to kafka producer

    Map<String,Object> producerConfigs(){
        Map<String,Object> prodConfig=new HashMap<>();
        prodConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        prodConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,keySerializer);
        prodConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,valueSerializer);
        prodConfig.put(ProducerConfig.ACKS_CONFIG,acks);
        prodConfig.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,deliveryTimeout);
        prodConfig.put(ProducerConfig.LINGER_MS_CONFIG,linger);
        prodConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,requestTimeout);
        prodConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,idempotence);
        prodConfig.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,inFlightRequests);
        prodConfig.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,transactionIdPrefix);

        return prodConfig;
    }

    @Bean
    ProducerFactory<String, Object> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String,Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    KafkaTransactionManager<String,Object>kafkaTransactionManager(ProducerFactory<String,Object>producerFactory){
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    NewTopic createWithdrawTopic() {
        return TopicBuilder.name(withdrawTopicName)
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    NewTopic createDepositTopic() {
        return TopicBuilder.name(depositTopicName)
                .partitions(3)
                .replicas(3)
                .build();
    }
}
