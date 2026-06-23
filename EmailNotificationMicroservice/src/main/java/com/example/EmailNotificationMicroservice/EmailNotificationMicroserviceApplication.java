package com.example.EmailNotificationMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EmailNotificationMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailNotificationMicroserviceApplication.class, args);
	}

    //external microservice
    //creates a new instance of rest template HttpClient, and it will put it into springApplication
    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
