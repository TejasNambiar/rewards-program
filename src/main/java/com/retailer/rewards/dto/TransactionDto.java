package com.retailer.rewards.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private Long id;
    private CustomerDto customer;
    private Double amount;
    private LocalDate transactionDate;
}