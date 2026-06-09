package com.retailer.rewards.service;

import com.retailer.rewards.dto.CustomerResponse;

import java.time.LocalDate;

public interface RewardService {
    CustomerResponse getCustomerRewards(Long customerId, LocalDate startDate, LocalDate endDate);
}
