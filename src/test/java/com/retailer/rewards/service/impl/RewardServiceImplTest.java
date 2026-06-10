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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        ReflectionTestUtils.setField(rewardsService, "loopbackMonths", 3);
        CustomerDto customer = new CustomerDto(1L, "Alice");

        TransactionDto t1 = new TransactionDto(1L, customer,120.0, LocalDate.of(2026, 6, 15)); // 90 points
        TransactionDto t2 = new TransactionDto(2L, customer, 80.0, LocalDate.of(2026, 5, 10));  // 30 points
        TransactionDto t3 = new TransactionDto(3L, customer, 40.0, LocalDate.of(2026, 5, 11));  // 0 points
        TransactionDto t4 = new TransactionDto(2L, customer, 80.0, LocalDate.of(2026, 4, 10));  // 30 points

        when(customerRepository.findCustomerById(1L)).thenReturn(customer);
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(any(), any(), any())).thenReturn(Arrays.asList(t1, t2, t3, t4));

        CustomerResponse response = rewardsService.getCustomerRewards(1L);

        assertEquals(190, response.getTotalPoints());
        assertEquals(3, response.getMonthlyRewards().size());
    }

    @Test
    void testCustomerNotFound_ThrowsException() {
        when(customerRepository.findCustomerById(99L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> rewardsService.getCustomerRewards(99L));
    }
}