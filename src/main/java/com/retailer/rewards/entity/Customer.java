package com.retailer.rewards.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a retail customer.
 */
@Entity
@Table(name = "customers")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
}
