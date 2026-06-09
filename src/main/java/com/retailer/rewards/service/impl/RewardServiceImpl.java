package com.retailer.rewards.service.impl;

import com.retailer.rewards.dto.CustomerDto;
import com.retailer.rewards.dto.CustomerResponse;
import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.exception.NotFoundException;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import com.retailer.rewards.service.RewardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

        System.out.print("Customer with ID "+customerId+" found: "+customer.toString());

        List<TransactionDto> transactions = transactionRepository
                .findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);

        System.out.print("Transactions found: "+transactions.size());

        CustomerResponse response = CustomerResponse.builder()
                .customerName(customer.getName())
                .customerId(customerId)
                .totalPoints(0).build();

        System.out.println("Customer Response: "+response.toString());

        return response;
    }
}
