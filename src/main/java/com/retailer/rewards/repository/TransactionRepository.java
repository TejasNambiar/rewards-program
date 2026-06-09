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

    /**
     * Finds all transactions for a specific customer within a given date range.
     *
     * @param customerId the ID of the customer
     * @param startDate  the start of the period
     * @param endDate    the end of the period
     * @return a list of matching transactions
     */
    @Query("Select new com.retailer.rewards.dto.TransactionDto(t.id, new com.retailer.rewards.dto.CustomerDto(t.customer.id, t.customer.name)" +
            ", t.amount, t.transactionDate) " +
            "FROM Transaction t " +
            "WHERE t.customer.id = :customerId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<TransactionDto> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}