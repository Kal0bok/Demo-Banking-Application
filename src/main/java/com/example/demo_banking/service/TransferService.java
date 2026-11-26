package com.example.demo_banking.service;

import com.example.demo_banking.dto.TransferRequest;
import com.example.demo_banking.model.Account;
import com.example.demo_banking.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountService accountService;
    private final AtomicLong transactionIdGenerator = new AtomicLong(1);

    public Transaction createTransfer(TransferRequest request) {
        Account fromAccount = accountService.getById(request.getFromAccountId());

        if (request.getToAccountId() != null && request.getToAccountId().equals(request.getFromAccountId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Account toAccount = request.getToAccountId() != null
                ? accountService.getById(request.getToAccountId())
                : null;

        synchronized (fromAccount) {
            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient funds on account id: " + fromAccount.getId());
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));

            Transaction outgoingTx = Transaction.builder()
                    .id(transactionIdGenerator.getAndIncrement())
                    .account(fromAccount)
                    .type("TRANSFER_OUT")
                    .amount(request.getAmount())
                    .createdAt(LocalDateTime.now())
                    .note(request.getNote())
                    .build();

            accountService.addTransaction(fromAccount.getId(), outgoingTx);

            if (toAccount != null) {
                synchronized (toAccount) {
                    toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

                    Transaction incomingTx = Transaction.builder()
                            .id(transactionIdGenerator.getAndIncrement())
                            .account(toAccount)
                            .type("TRANSFER_IN")
                            .amount(request.getAmount())
                            .createdAt(LocalDateTime.now())
                            .note(request.getNote())
                            .build();

                    accountService.addTransaction(toAccount.getId(), incomingTx);
                }
            }

            return outgoingTx;
        }
    }
}