package com.retailer.rewards.service;

import com.retailer.rewards.dto.CustomerResponse;

public interface RewardService {
    /**
     * Retrieves reward points for a customer based on the configured configuration window.
     *
     * @param customerId the unique identifier of the target customer
     * @return a CustomerResponse detailing monthly points metrics
     */
    CustomerResponse getCustomerRewards(Long customerId);
}
