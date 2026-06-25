package com.example.TransferMicroservice.service.impl;

import com.example.TransferMicroservice.exception.TransferException;
import com.example.TransferMicroservice.model.TransferModel;
import com.example.TransferMicroservice.service.TransferService;
import com.example.coreModule.event.DepositRequestedEvent;
import com.example.coreModule.event.WithdrawalRequestedEvent;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TransferServiceImpl implements TransferService {

    private KafkaTemplate<String,Object> kafkaTemplate;
    private Environment environment;
    private RestTemplate restTemplate;

    public TransferServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, Environment environment, RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Transactional
    @Override
    public boolean transfer(TransferModel transferModel) {
        WithdrawalRequestedEvent withdrawalEvent=new WithdrawalRequestedEvent(transferModel.getSenderId(),
                transferModel.getRecipientId(),transferModel.getAmount());
        DepositRequestedEvent depositEvent=new DepositRequestedEvent(transferModel.getSenderId(),
                transferModel.getRecipientId(),transferModel.getAmount());
        try{
            kafkaTemplate.send(environment.getProperty("withdraw-money-topic","withdraw-money-topic"),
                    withdrawalEvent);
            log.info("Sent event to withdrawal topic");

            callRemoteService();

            kafkaTemplate.send(environment.getProperty("deposit-money-topic","deposit-money-topic"),
                    depositEvent);
            log.info("Sent event to deposit topic");
        }catch (Exception exp){
            log.error(exp.getMessage(),exp);
            throw new TransferException(exp);
        }
        return true;
    }

    private ResponseEntity<String> callRemoteService() throws Exception{
        String requestedUrl="http://localhost:8082/response/200";
        ResponseEntity<String> response=restTemplate.exchange(requestedUrl, HttpMethod.GET,null,String.class);
        if(response.getStatusCode().value()== HttpStatus.SERVICE_UNAVAILABLE.value()){
            throw new Exception("Destination microservice not available");
        }
        if(response.getStatusCode().value()== HttpStatus.OK.value()){
            log.info("Received response from mock service: "+response.getBody());
        }
        return response;
    }
}
