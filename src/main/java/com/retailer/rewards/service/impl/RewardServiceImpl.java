package com.retailer.rewards.service.impl;

import com.retailer.rewards.dto.CustomerDto;
import com.retailer.rewards.dto.CustomerResponse;
import com.retailer.rewards.dto.MonthReward;
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

        List<MonthReward> monthlyRewards = pointsByMonth.entrySet().stream()
                .map(entry -> new MonthReward(entry.getKey(), entry.getValue()))
                .toList();

        int totalPoints = monthlyRewards.stream().mapToInt(MonthReward::getPoints).sum();
        System.out.println("Total points calculated: "+totalPoints);

        CustomerResponse response = CustomerResponse.builder()
                .customerName(customer.getName())
                .customerId(customerId)
                .monthlyRewards(monthlyRewards)
                .totalPoints(totalPoints).build();

        System.out.println("Customer Response: "+response.toString());

        return response;
    }

    private int calculatePoints(BigDecimal amount) {
        if(amount == null || amount.compareTo(BigDecimal.valueOf(50)) < 0){
            return 0;
        }
        int value = amount.intValue();
        System.out.println("Calculating points for amount: "+amount+" with integer value: "+value);
        int points = 0;
        if(value >=50 && value <=100){
            System.out.println("Amount between 50 and 100, awarding 50 points.");
            points += 50;
        }else if(value > 100){
            System.out.println("Amount above 100, awarding 50 points for first 100 and 2 points for each dollar above 100.");
            int extra = value - 100;
            points = extra * 2 + 50;
        }
        System.out.println("Points calculated: "+points);
        return points;
    }
}
