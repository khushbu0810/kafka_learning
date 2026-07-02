package com.example.PaymentMicroservice.service.impl;

import com.example.PaymentMicroservice.service.CreditCardProcessorRemoteService;
import com.example.core.dto.CreditCardProcessorRequest;
import com.example.core.exception.CreditCardProcessorUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class CreditCardProcessorRemoteServiceImpl implements CreditCardProcessorRemoteService {
    private final String ccpRemoteServiceUrl;

    private RestTemplate restTemplate=new RestTemplate();


    public CreditCardProcessorRemoteServiceImpl(
            @Value("${remote.ccp.url}") String ccpRemoteServiceUrl
    ) {
        this.ccpRemoteServiceUrl = ccpRemoteServiceUrl;
    }


    @Override
    public void process(BigInteger cardNumber, BigDecimal paymentAmount) {
        try {
            var request = new CreditCardProcessorRequest(cardNumber, paymentAmount);
            restTemplate.postForObject(ccpRemoteServiceUrl + "/ccp/process", request, CreditCardProcessorRequest.class);
        } catch (ResourceAccessException e) {
            throw new CreditCardProcessorUnavailableException(e);
        }
    }
}