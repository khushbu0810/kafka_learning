package com.example.ProductsMicroservice.service;

import com.example.ProductsMicroservice.model.Product;
import com.example.core.event.ProductCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@DirtiesContext //code may be modified during execution of test (make sure each test will start with a clean slate)
//helpful when test class contains more than one test methods (only one instance for this whole class will be created when test methods executes)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") //test profile -> will look for application-test.properties
// (no of partitions in topic), (no of brokers to start) (if broker shutdown then shift to another available broker)
//@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = true)
@EmbeddedKafka(partitions = 3, controlledShutdown = true)
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private Environment environment;

    KafkaMessageListenerContainer<String, ProductCreatedEvent> container;
    private BlockingQueue<ConsumerRecord<String, ProductCreatedEvent>> records;

    @BeforeAll
    void setUp() {
        //create kafka consumers using config properties
        DefaultKafkaConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());

        //from which kafka topics messages will be consumed using container properties
        ContainerProperties containerProperties = new ContainerProperties(Objects.requireNonNull(environment.getProperty("product-created-events-topic-name")));

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, ProductCreatedEvent>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @Test
    void testCreateProduct_whenGivenValidProductDetails_successfulSendKafkaMessage() throws Exception {
        //Arrange
        //1. create product method takes input of Product type
        String title = "iphone 11";
        BigDecimal price = new BigDecimal(600);
        Integer quantity = 1;

        //2. creating Product object
        Product product = new Product();
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setTitle(title);

        //Act
        //3. invoking method
        productService.createProduct(product);


    }

    //this method return Map of configuration properties
    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, Objects.requireNonNull(environment.getProperty("spring.kafka.consumer.group-id")),
                JacksonJsonDeserializer.TRUSTED_PACKAGES, Objects.requireNonNull(environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages")),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Objects.requireNonNull(environment.getProperty("spring.kafka.consumer.auto-offset-reset"))
        );
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }
}
