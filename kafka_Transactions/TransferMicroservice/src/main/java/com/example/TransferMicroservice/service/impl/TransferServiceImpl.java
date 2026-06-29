package com.example.TransferMicroservice.service.impl;

import com.example.TransferMicroservice.exception.TransferException;
import com.example.TransferMicroservice.model.TransferEntity;
import com.example.TransferMicroservice.model.TransferModel;
import com.example.TransferMicroservice.repository.TransferRepo;
import com.example.TransferMicroservice.service.TransferService;
import com.example.coreModule.event.DepositRequestedEvent;
import com.example.coreModule.event.WithdrawalRequestedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Slf4j
public class TransferServiceImpl implements TransferService {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private Environment environment;
    private TransferRepo transferRepo;
    //    private RestTemplate restTemplate;
//
//    public TransferServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, Environment environment, RestTemplate restTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.environment = environment;
//        this.restTemplate = restTemplate;
//    }
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, Environment environment, TransferRepo transferRepo) {
        this.kafkaTemplate = kafkaTemplate;
        this.environment = environment;
        this.transferRepo = transferRepo;
    }

    //    @Transactional(value="kafkaTransactionManager",rollbackFor = {TransferException.class, ConnectException.class})
//    @Transactional("kafkaTransactionManager") , now we have db save logic in method so we need to use transactionManager
    @Transactional("transactionManager")
    @Override
    public boolean transfer(TransferModel transferModel) {
        /*
        consumer microservice receive both of messages for withdraw and deposit if successful OR NONE.
         */
        WithdrawalRequestedEvent withdrawalEvent = new WithdrawalRequestedEvent(transferModel.getSenderId(),
                transferModel.getRecipientId(), transferModel.getAmount());
        DepositRequestedEvent depositEvent = new DepositRequestedEvent(transferModel.getSenderId(),
                transferModel.getRecipientId(), transferModel.getAmount());

        TransferEntity transferEntity = new TransferEntity();
        //copying values from model to entity -> (src,dest)
        BeanUtils.copyProperties(transferModel, transferEntity);
        transferEntity.setTransferId(UUID.randomUUID().toString());

        try {
            ///saving to database
            transferRepo.save(transferEntity);

            kafkaTemplate.send(environment.getProperty("withdraw-money-topic", "withdraw-money-topic"),
                    withdrawalEvent);
            log.info("Sent event to withdrawal topic");

            callRemoteService();

            kafkaTemplate.send(environment.getProperty("deposit-money-topic", "deposit-money-topic"),
                    depositEvent);
            log.info("Sent event to deposit topic");
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
            throw new TransferException(exp);
        }
        return true;
    }

    private ResponseEntity<String> callRemoteService() throws Exception {
        String requestedUrl = "http://localhost:8082/response/200";
        ResponseEntity<String> response = restTemplate.exchange(requestedUrl, HttpMethod.GET, null, String.class);
        if (response.getStatusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            throw new Exception("Destination microservice not available");
        }
        if (response.getStatusCode().value() == HttpStatus.OK.value()) {
            log.info("Received response from mock service: " + response.getBody());
        }
        return response;
    }
}
