package com.example.demo_banking.controller;

import com.example.demo_banking.dto.TransferRequest;
import com.example.demo_banking.model.Transaction;
import com.example.demo_banking.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Transaction> createTransfer(@Valid @RequestBody TransferRequest request) {
        Transaction transaction = transferService.createTransfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
}