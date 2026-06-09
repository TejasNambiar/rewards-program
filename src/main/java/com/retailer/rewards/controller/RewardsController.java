package com.retailer.rewards.controller;

import com.retailer.rewards.dto.CustomerResponse;
import com.retailer.rewards.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST Endpoint for accessing calculated customer rewards datasets.
 */
@RestController
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    private final RewardService rewardsService;

    public RewardsController(RewardService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getRewards(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot occur prior to start date.");
        }

        CustomerResponse response = rewardsService.getCustomerRewards(customerId, start, end);
        return ResponseEntity.ok(response);
    }
}