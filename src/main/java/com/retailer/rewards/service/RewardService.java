package com.retailer.rewards.service;

import com.retailer.rewards.dto.CustomerResponse;

import java.time.LocalDate;

public interface RewardService {
    /**
     * Calculates monthly and total reward points for a customer over a specified date range.
     *
     * @param customerId the ID of the customer
     * @param startDate  the start date of the evaluation window
     * @param endDate    the end date of the evaluation window
     * @return complete rewards breakdown DTO
     */
    CustomerResponse getCustomerRewards(Long customerId, LocalDate startDate, LocalDate endDate);
}
