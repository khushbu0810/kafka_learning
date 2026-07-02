package com.example.CreditCardProcessorMicroservice.controller;

import com.example.core.dto.CreditCardProcessorRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ccp")
@Slf4j
public class CreditCardProcessorController {

    @PostMapping("/process")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void processCreditCard(@RequestBody @Valid CreditCardProcessorRequest request) {
        log.info("Processing request: {}", request);
    }
}
