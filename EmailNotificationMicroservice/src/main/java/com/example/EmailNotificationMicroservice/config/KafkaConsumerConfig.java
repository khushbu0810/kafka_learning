package com.example.EmailNotificationMicroservice.config;

import com.example.EmailNotificationMicroservice.exception.NotRetryableException;
import com.example.EmailNotificationMicroservice.exception.RetryableException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Autowired
    Environment environment;

    /*
invalid json format
1:{sbcfijfkmf}
we need to deserialize the message to handle errors  -> value deserializer (error handling deserializer) --> catch any deserialization exception

ErrorHandlingDeserializer ==>
 //value deserializer class to error handling deserializer which will catch any deserialization exception

JacksonJsonDeserializer ==>
 //actual deserialization should be done using json deserializer


after this error handling deserializer application will not stop receiving message ...
it give error for the wrong message and if good message comes it consumes it ..

     */

    @Bean
    public ConsumerFactory<String,Object> consumerFactory(){
        Map<String,Object> ConConfig=new HashMap<>();
//        ConConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers); //@Value
        ConConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,environment.getProperty("spring.kafka.consumer.bootstrap-servers")); //another way
        ConConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        ConConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class); //another way
        ConConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        ConConfig.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,JacksonJsonDeserializer.class);
        ConConfig.put(JacksonJsonDeserializer.TRUSTED_PACKAGES,environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
        ConConfig.put(ConsumerConfig.GROUP_ID_CONFIG,environment.getProperty("spring.kafka.consumer.group-id"));
        return new DefaultKafkaConsumerFactory<>(ConConfig);
    }


    //kafka listener container is responsible for receiving messages from kafka topic and invoking the handler method
    @Bean
    ConcurrentKafkaListenerContainerFactory<String,Object> kafkaListenerContainerFactory(ConsumerFactory<String,Object> consumerFactory,KafkaTemplate<String,Object> kafkaTemplate){

        //DeadLetterPublishingRecoverer -> used to send failed messages to dead letter topic  ---> receives kafkaTemplate as argument
        //used to handle exceptions that occur during message consumption by kafka listener
        DefaultErrorHandler errorHandler=new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(5000,3));
        errorHandler.addNotRetryableExceptions(NotRetryableException.class);
        errorHandler.addRetryableExceptions(RetryableException.class);

        ConcurrentKafkaListenerContainerFactory<String,Object> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    KafkaTemplate<String,Object> kafkaTemplate(ProducerFactory<String,Object> producerFactory){
        return new KafkaTemplate<>(producerFactory); //used to create kafka producer
    }

    @Bean
    ProducerFactory<String,Object>producerFactory(){
        Map<String,Object> conConfig=new HashMap<>();
        conConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        conConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        conConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(conConfig);
    }
}
