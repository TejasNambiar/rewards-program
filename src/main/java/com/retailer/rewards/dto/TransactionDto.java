package com.retailer.rewards.dto;

import com.retailer.rewards.entity.Customer;
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
    private Customer customer;
    private BigDecimal amount;
    private LocalDate transactionDate;
}