package com.retailer.rewards.service.impl;

import com.retailer.rewards.dto.CustomerDto;
import com.retailer.rewards.dto.CustomerResponse;
import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.exception.NotFoundException;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class RewardsServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private RewardServiceImpl rewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculatePoints_CorrectCalculation() {
        CustomerDto customer = new CustomerDto(1L, "Alice");
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 3, 31);

        TransactionDto t1 = new TransactionDto(1L, customer, BigDecimal.valueOf(120), LocalDate.of(2026, 1, 15)); // 90 points
        TransactionDto t2 = new TransactionDto(2L, customer, BigDecimal.valueOf(80), LocalDate.of(2026, 2, 10));  // 30 points
        TransactionDto t3 = new TransactionDto(3L, customer, BigDecimal.valueOf(40), LocalDate.of(2026, 2, 11));  // 0 points

        when(customerRepository.findCustomerById(1L)).thenReturn(customer);
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(1L, start, end)).thenReturn(Arrays.asList(t1, t2, t3));

        CustomerResponse response = rewardsService.getCustomerRewards(1L, start, end);

        assertEquals(140, response.getTotalPoints());
        assertEquals(2, response.getMonthlyRewards().size());
    }

    @Test
    void testCustomerNotFound_ThrowsException() {
        when(customerRepository.findCustomerById(99L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> rewardsService.getCustomerRewards(99L, LocalDate.now(), LocalDate.now()));
    }
}