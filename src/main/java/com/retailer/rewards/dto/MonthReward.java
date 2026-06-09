package com.retailer.rewards.dto;

import lombok.*;

/**
 * DTO representing points earned in a specific month.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthReward {
    private String month;
    private int points;
}