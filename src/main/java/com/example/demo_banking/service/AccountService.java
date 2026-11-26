package com.example.demo_banking.service;

import com.example.demo_banking.dto.CreateAccountRequest;
import com.example.demo_banking.model.Account;
import com.example.demo_banking.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private final Map<Long, List<Transaction>> transactionsByAccountId = new ConcurrentHashMap<>();
    private final AtomicLong accountIdGenerator = new AtomicLong(1);

    public Account createAccount(CreateAccountRequest request) {
        long id = accountIdGenerator.getAndIncrement();
        Account account = Account.builder()
                .id(id)
                .iban(request.getIban())
                .ownerName(request.getOwnerName())
                .balance(request.getBalance())
                .build();

        accounts.put(id, account);
        transactionsByAccountId.put(id, new ArrayList<>());
        return account;
    }

    public Account getById(Long id) {
        Account account = accounts.get(id);
        if (account == null) {
            throw new NoSuchElementException("Account not found with id: " + id);
        }
        return account;
    }

    public List<Transaction> getTransactions(Long accountId) {
        getById(accountId);
        return transactionsByAccountId.getOrDefault(accountId, List.of());
    }

    public void addTransaction(Long accountId, Transaction transaction) {
        getById(accountId);
        transactionsByAccountId.computeIfAbsent(accountId, k -> new ArrayList<>()).add(transaction);
    }
}