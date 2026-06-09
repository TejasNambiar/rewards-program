package com.retailer.rewards.service.impl;

import com.retailer.rewards.dto.CustomerDto;
import com.retailer.rewards.dto.CustomerResponse;
import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.exception.NotFoundException;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import com.retailer.rewards.service.RewardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardServiceImpl(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public CustomerResponse getCustomerRewards(Long customerId, LocalDate startDate, LocalDate endDate) {
        CustomerDto customer = customerRepository.findCustomerById(customerId);
        if(customer == null){
            throw new NotFoundException("Customer not found with ID: " + customerId);
        }

        System.out.println("Customer with ID "+customerId+" found: "+customer.getName());

        List<TransactionDto> transactions = transactionRepository
                .findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);

        System.out.println("Transactions found: "+transactions.size());

        // Group transactions dynamically by Month Name to comply with non-hardcoded requirements
        Map<String, Integer> pointsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionDate().getMonth().name(),
                        Collectors.summingInt(t -> calculatePoints(t.getAmount()))
                ));

        System.out.println("Map: "+ pointsByMonth);

        CustomerResponse response = CustomerResponse.builder()
                .customerName(customer.getName())
                .customerId(customerId)
                .totalPoints(0).build();

        System.out.println("Customer Response: "+response.toString());

        return response;
    }

    private int calculatePoints(BigDecimal amount) {
        return 1;
    }
}
