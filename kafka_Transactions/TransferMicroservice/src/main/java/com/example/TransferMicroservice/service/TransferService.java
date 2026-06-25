package com.example.TransferMicroservice.service;

import com.example.TransferMicroservice.model.TransferModel;

public interface TransferService {
    public boolean transfer(TransferModel productPaymentModel);
}
