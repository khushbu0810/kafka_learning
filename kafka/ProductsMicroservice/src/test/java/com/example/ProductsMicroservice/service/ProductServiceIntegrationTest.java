package com.example.ProductsMicroservice.service;

import com.example.ProductsMicroservice.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

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

    @Test
    void testCreateProduct_whenGivenValidProductDetails_successfulSendKafkaMessage() throws Exception {
        //Arrange
        //1. create product method takes input of Product type
        String title="iphone 11";
        BigDecimal price=new BigDecimal(600);
        Integer quantity=1;

        //2. creating Product object
        Product product=new Product();
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setTitle(title);

        //Act
        //3. invoking method
        productService.createProduct(product);


    }
}
