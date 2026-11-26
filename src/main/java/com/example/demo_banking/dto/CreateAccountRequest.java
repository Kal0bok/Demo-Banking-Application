package com.example.demo_banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    @NotBlank(message = "IBAN is required")
    private String iban;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance must be zero or positive")
    private BigDecimal balance;
}