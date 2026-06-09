package com.retailer.rewards.controller;

import com.retailer.rewards.dto.CustomerResponse;
import com.retailer.rewards.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST Controller that exposes endpoints for the Retailer Rewards Program.
 * This controller handles incoming HTTP requests to calculate and retrieve
 * reward points for customers based on their transaction history.
 */
@RestController
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    private final RewardService rewardsService;

    public RewardsController(RewardService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Retrieves the reward points earned by a specific customer within a given date range.
     * The response includes a monthly breakdown of points and the cumulative total.
     *
     * Endpoint: GET /api/v1/rewards/{customerId}?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
     *
     * @param customerId the unique ID of the customer
     * @param startDate  the start of the three-month (or custom) period in ISO format (yyyy-MM-dd)
     * @param endDate    the end of the period in ISO format (yyyy-MM-dd)
     * @return a {@link ResponseEntity} containing the {@link CustomerResponse} with point details
     * @throws IllegalArgumentException if the end date is chronologically before the start date
     * @throws java.time.format.DateTimeParseException if the date strings are not in the correct format
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getRewards(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Validation for logical date sequence
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot occur prior to start date.");
        }

        CustomerResponse response = rewardsService.getCustomerRewards(customerId, start, end);
        return ResponseEntity.ok(response);
    }
}