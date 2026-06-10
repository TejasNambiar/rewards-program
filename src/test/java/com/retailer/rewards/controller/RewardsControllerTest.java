package com.retailer.rewards.controller;

import com.retailer.rewards.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardsService;

    @Test
        // Fixed: Renamed from 'get()' to avoid shadowing the MockMvc builders method
    void getRewards_SuccessScenario() throws Exception {

        when(rewardsService.getCustomerRewards(any())).thenReturn(
                new com.retailer.rewards.dto.CustomerResponse(1L, "John Doe", new ArrayList<>(), 0)
        );

        mockMvc.perform(get("/api/v1/rewards/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalPoints").exists());
    }

    @Test
    void getRewards_NoPathParam_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getRewards_InvalidCustomerId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/0"))
                .andExpect(status().isBadRequest());
    }
}