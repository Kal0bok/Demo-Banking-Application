package com.example.demo_banking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull(message = "fromAccountId is required")
    private Long fromAccountId;

    private Long toAccountId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "note must be at most 255 characters")
    private String note;
}