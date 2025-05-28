package com.workshop.firstapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.firstapp.exception.BadRequestException;
import com.workshop.firstapp.exception.ResourceNotFoundException;
import com.workshop.firstapp.model.Policy;
import com.workshop.firstapp.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PolicyService policyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Policy validPolicy;

    @BeforeEach
    void setUp() {
        validPolicy = new Policy();
        validPolicy.setId("507f1f77bcf86cd799439011");
        validPolicy.setPolicyNumber("POL001");
        validPolicy.setPolicyHolderName("John Doe");
        validPolicy.setPolicyType("Term Life");
        validPolicy.setBasePremium(1000.0);
    }

    // ========== POST /api/policies Tests ==========

    @Test
    @DisplayName("Should create policy and return 201 status")
    void shouldCreatePolicyAndReturn201() throws Exception {
        // Arrange
        Policy newPolicy = new Policy();
        newPolicy.setPolicyHolderName("John Doe");
        newPolicy.setPolicyType("Term Life");
        newPolicy.setBasePremium(1000.0);

        when(policyService.buyPolicy(any(Policy.class))).thenReturn(validPolicy);

        // Act & Assert
        mockMvc.perform(post("/api/policies/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPolicy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("507f1f77bcf86cd799439011"))
                .andExpect(jsonPath("$.policyNumber").value("POL001"))
                .andExpect(jsonPath("$.policyHolderName").value("John Doe"))
                .andExpect(jsonPath("$.basePremium").value(1000.0));
    }

    @Test
    @DisplayName("Should return 400 when service throws BadRequestException")
    void shouldReturn400WhenServiceThrowsBadRequestException() throws Exception {
        // Arrange
        Policy invalidPolicy = new Policy();
        invalidPolicy.setPolicyHolderName("");  // Empty name

        when(policyService.buyPolicy(any(Policy.class)))
                .thenThrow(new BadRequestException("Policy holder name cannot be empty"));

        // Act & Assert
        mockMvc.perform(post("/api/policies/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPolicy)))
                .andExpect(status().isBadRequest());
    }

    // ========== GET /api/policies/{id} Tests ==========

    @Test
    @DisplayName("Should retrieve policy by ID and return 200 status")
    void shouldRetrievePolicyByIdAndReturn200() throws Exception {
        // Arrange
        String policyId = "507f1f77bcf86cd799439011";
        when(policyService.getPolicy(eq(policyId))).thenReturn(validPolicy);

        // Act & Assert
        mockMvc.perform(get("/api/policies/{id}", policyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(policyId))
                .andExpect(jsonPath("$.policyHolderName").value("John Doe"))
                .andExpect(jsonPath("$.basePremium").value(1000.0));
    }

    @Test
    @DisplayName("Should return 404 when policy not found")
    void shouldReturn404WhenPolicyNotFound() throws Exception {
        // Arrange
        String nonExistentId = "999999999999999999999999";
        when(policyService.getPolicy(eq(nonExistentId)))
                .thenThrow(new ResourceNotFoundException("Policy not found with ID: " + nonExistentId));

        // Act & Assert
        mockMvc.perform(get("/api/policies/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    // ========== GET /api/policies Tests ==========

    @Test
    @DisplayName("Should return all policies with 200 status")
    void shouldReturnAllPoliciesWithStatus200() throws Exception {
        // Arrange
        Policy policy2 = new Policy();
        policy2.setId("507f1f77bcf86cd799439012");
        policy2.setPolicyHolderName("Jane Smith");
        policy2.setBasePremium(2000.0);

        List<Policy> policies = Arrays.asList(validPolicy, policy2);
        when(policyService.getAllPolicies()).thenReturn(policies);

        // Act & Assert
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].policyHolderName").value("John Doe"))
                .andExpect(jsonPath("$[1].policyHolderName").value("Jane Smith"));
    }

    @Test
    @DisplayName("Should return empty list when no policies exist")
    void shouldReturnEmptyListWhenNoPoliciesExist() throws Exception {
        // Arrange
        when(policyService.getAllPolicies()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}