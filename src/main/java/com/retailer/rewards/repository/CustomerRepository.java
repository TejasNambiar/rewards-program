package com.retailer.rewards.repository;

import com.retailer.rewards.dto.CustomerDto;
import com.retailer.rewards.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Customer entity operations.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT new com.retailer.rewards.dto.CustomerDto(c.id, c.name) FROM Customer c WHERE c.id = :customerId")
    CustomerDto findCustomerById(Long customerId);
}