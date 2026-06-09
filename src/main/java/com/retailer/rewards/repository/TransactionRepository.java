package com.retailer.rewards.repository;

import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("Select new com.retailer.rewards.dto.TransactionDto(t.id, new com.retailer.rewards.dto.CustomerDto(t.customer.id, t.customer.name)" +
            ", t.amount, t.transactionDate) " +
            "FROM Transaction t " +
            "WHERE t.customer.id = :customerId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<TransactionDto> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}