package com.example.TransferMicroservice.controller;

import com.example.TransferMicroservice.model.TransferModel;
import com.example.TransferMicroservice.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
@Slf4j
public class TransferController {
    private TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public boolean transfer(@RequestBody TransferModel transferModel){
        return transferService.transfer(transferModel);
    }
}
