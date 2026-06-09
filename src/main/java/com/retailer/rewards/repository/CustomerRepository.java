package com.retailer.rewards.repository;

import com.retailer.rewards.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Customer entity operations.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}