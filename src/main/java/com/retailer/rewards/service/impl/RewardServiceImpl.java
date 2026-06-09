package com.retailer.rewards.service.impl;

import com.retailer.rewards.dto.CustomerDto;
import com.retailer.rewards.dto.CustomerResponse;
import com.retailer.rewards.dto.MonthReward;
import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.exception.NotFoundException;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import com.retailer.rewards.service.RewardService;
import com.retailer.rewards.util.LoggerUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for managing and calculating customer reward points.
 * Provides capabilities to aggregate transactions over flexible date ranges
 * and break down point allocations on a per-month and total basis.
 * * @author Web API Developer
 * @version 1.0
 */
@Service
public class RewardServiceImpl implements RewardService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardServiceImpl(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves transactions for a specified customer within a date window, computes
     * the reward points earned per calendar month, and returns a summarized breakdown.
     *
     * @param customerId the unique identifier of the target customer
     * @param startDate  the start date of the reporting window (inclusive)
     * @param endDate    the end date of the reporting window (inclusive)
     * @return a {@link CustomerResponse} detailing monthly points metrics and cumulative point total
     * @throws NotFoundException if no customer matches the provided customerId
     */
    @Override
    public CustomerResponse getCustomerRewards(Long customerId, LocalDate startDate, LocalDate endDate) {
        // 1. Verify customer existence
        CustomerDto customer = customerRepository.findCustomerById(customerId);
        if(customer == null){
            throw new NotFoundException("Customer not found with ID: " + customerId);
        }

        LoggerUtil.info(RewardServiceImpl.class, "Customer with ID {} found: {}", customerId, customer.getName());

        // 2. Query transactions matching criteria across the specified historical window
        List<TransactionDto> transactions = transactionRepository
                .findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);

        LoggerUtil.info(RewardServiceImpl.class, "Transactions found: {}", transactions.size());

        // 3. Group transactions dynamically by Month Name to comply with non-hardcoded requirements
        Map<String, Integer> pointsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionDate().getMonth().name(),
                        Collectors.summingInt(t -> calculatePoints(t.getAmount()))
                ));

        LoggerUtil.info(RewardServiceImpl.class, "Map: {}", pointsByMonth);

        // 4. Transform internal groupings map into API-compliant DTO structures
        List<MonthReward> monthlyRewards = pointsByMonth.entrySet().stream()
                .map(entry -> new MonthReward(entry.getKey(), entry.getValue()))
                .toList();

        // 5. Aggregate overall point totals across all processed billing windows
        int totalPoints = monthlyRewards.stream().mapToInt(MonthReward::getPoints).sum();
        LoggerUtil.info(RewardServiceImpl.class, "Total points calculated: {}", totalPoints);

        // 6. Construct and map the finalized structural API payload response
        CustomerResponse response = CustomerResponse.builder()
                .customerName(customer.getName())
                .customerId(customerId)
                .monthlyRewards(monthlyRewards)
                .totalPoints(totalPoints).build();

        LoggerUtil.info(RewardServiceImpl.class, "Customer Response: {}", response);

        return response;
    }

    /**
     * Algorithmic helper calculating reward points based on specific price thresholds.
     * Rules applied:
     * - 2 points for every dollar spent over $100
     * - 1 point for every dollar spent between $50 and $100
     *
     * @param amount the raw monetary value of a discrete transaction purchase
     * @return calculated integer reward points allocation value (defaults to 0 if under limits)
     */
    private int calculatePoints(BigDecimal amount) {
        if(amount == null || amount.compareTo(BigDecimal.valueOf(50)) < 0){
            return 0;
        }
        int value = amount.intValue();
        LoggerUtil.info(RewardServiceImpl.class, "Calculating points for amount: {} with integer value: {}", amount, value);
        int points = 0;

        // Scenario A: Amount falls within the intermediate $50 to $100 threshold range
        if(value >=50 && value <=100){
            LoggerUtil.info(RewardServiceImpl.class, "Amount between 50 and 100, awarding 50 points.");
            points += 50;
        }
        // Scenario B: Amount crosses the premium $100 tier threshold
        else if(value > 100){
            LoggerUtil.info(RewardServiceImpl.class, "Amount above 100, awarding 50 points for first 100 and 2 points for each dollar above 100.");
            int extra = value - 100;
            points = extra * 2 + 50; // 2 points for each dollar above 100 + 50 flat points for the intermediate tier ($50-$100)
        }
        LoggerUtil.info(RewardServiceImpl.class, "Points calculated: {}", points);
        return points;
    }
}
