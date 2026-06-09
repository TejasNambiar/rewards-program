package com.retailer.rewards.dto;

import lombok.*;

/**
 * Entity representing a retail customer.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
}
