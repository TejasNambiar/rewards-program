package com.retailer.rewards.dto;

import lombok.*;

/**
 * API Response payload carrying monthly breakdowns and total rewards points.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long customerId;
    private String customerName;
    private int totalPoints;
}