package com.workshop.firstapp.service;

import com.workshop.firstapp.exception.BadRequestException;
import com.workshop.firstapp.exception.ResourceNotFoundException;
import com.workshop.firstapp.model.Policy;
import com.workshop.firstapp.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PolicyService policyService;

    private Policy validPolicy;

    @BeforeEach
    void setUp() {
        // Arrange common test data
        validPolicy = new Policy();
        validPolicy.setId("507f1f77bcf86cd799439011");
        validPolicy.setPolicyNumber("POL001");
        validPolicy.setPolicyHolderName("John Doe");
        validPolicy.setPolicyType("Term Life");
        validPolicy.setBasePremium(1000.0);
    }

    // ========== Create Policy Tests ==========

    @Test
    @DisplayName("Should create policy successfully with valid data")
    void shouldCreatePolicySuccessfully() {
        // Arrange
        Policy newPolicy = new Policy();
        newPolicy.setPolicyHolderName("John Doe");
        newPolicy.setPolicyType("Term Life");
        newPolicy.setBasePremium(1000.0);

        when(policyRepository.save(any(Policy.class))).thenReturn(validPolicy);

        // Act
        Policy result = policyService.buyPolicy(newPolicy);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("507f1f77bcf86cd799439011");
        assertThat(result.getPolicyNumber()).isEqualTo("POL001");
        assertThat(result.getPolicyHolderName()).isEqualTo("John Doe");
        assertThat(result.getBasePremium()).isEqualTo(1000.0);

        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    @DisplayName("Should generate unique policy numbers")
    void shouldGenerateUniquePolicyNumbers() {
        // Arrange
        Policy policy1 = new Policy();
        policy1.setPolicyHolderName("John Doe");
        policy1.setBasePremium(1000.0);

        Policy policy2 = new Policy();
        policy2.setPolicyHolderName("Jane Smith");
        policy2.setBasePremium(2000.0);

        Policy saved1 = new Policy();
        saved1.setPolicyNumber("POL001");
        saved1.setPolicyHolderName("John Doe");

        Policy saved2 = new Policy();
        saved2.setPolicyNumber("POL002");
        saved2.setPolicyHolderName("Jane Smith");

        when(policyRepository.save(any(Policy.class)))
                .thenReturn(saved1)
                .thenReturn(saved2);

        // Act
        Policy result1 = policyService.buyPolicy(policy1);
        Policy result2 = policyService.buyPolicy(policy2);

        // Assert
        assertThat(result1.getPolicyNumber()).isNotNull();
        assertThat(result2.getPolicyNumber()).isNotNull();
        assertThat(result1.getPolicyNumber()).isNotEqualTo(result2.getPolicyNumber());
    }

    // ========== Validation Tests ==========

    @Test
    @DisplayName("Should throw exception when policy holder name is null")
    void shouldThrowExceptionForNullPolicyHolderName() {
        // Arrange
        Policy invalidPolicy = new Policy();
        invalidPolicy.setPolicyHolderName(null);  // Null name
        invalidPolicy.setPolicyType("Term Life");
        invalidPolicy.setBasePremium(1000.0);

        // Act & Assert
        assertThatThrownBy(() -> policyService.buyPolicy(invalidPolicy))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Policy holder name cannot be null");

        verify(policyRepository, never()).save(any(Policy.class));
    }

    @Test
    @DisplayName("Should throw exception when policy holder name is empty")
    void shouldThrowExceptionForEmptyPolicyHolderName() {
        // Arrange
        Policy invalidPolicy = new Policy();
        invalidPolicy.setPolicyHolderName("");  // Empty name
        invalidPolicy.setPolicyType("Term Life");
        invalidPolicy.setBasePremium(1000.0);

        // Act & Assert
        assertThatThrownBy(() -> policyService.buyPolicy(invalidPolicy))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Policy holder name cannot be null or empty");

        verify(policyRepository, never()).save(any(Policy.class));
    }

    @Test
    @DisplayName("Should throw exception for negative premium")
    void shouldThrowExceptionForNegativePremium() {
        // Arrange
        Policy invalidPolicy = new Policy();
        invalidPolicy.setPolicyHolderName("John Doe");
        invalidPolicy.setPolicyType("Term Life");
        invalidPolicy.setBasePremium(-100.0);  // Negative premium

        // Act & Assert
        assertThatThrownBy(() -> policyService.buyPolicy(invalidPolicy))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Premium must be positive");

        verify(policyRepository, never()).save(any(Policy.class));
    }

    @Test
    @DisplayName("Should throw exception for zero premium")
    void shouldThrowExceptionForZeroPremium() {
        // Arrange
        Policy invalidPolicy = new Policy();
        invalidPolicy.setPolicyHolderName("John Doe");
        invalidPolicy.setPolicyType("Term Life");
        invalidPolicy.setBasePremium(0.0);  // Zero premium

        // Act & Assert
        assertThatThrownBy(() -> policyService.buyPolicy(invalidPolicy))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Premium must be positive");

        verify(policyRepository, never()).save(any(Policy.class));
    }

    // ========== Retrieval Tests ==========

    @Test
    @DisplayName("Should retrieve policy by ID successfully")
    void shouldRetrievePolicyByIdSuccessfully() {
        // Arrange
        String policyId = "507f1f77bcf86cd799439011";
        when(policyRepository.findById(policyId)).thenReturn(Optional.of(validPolicy));

        // Act
        Policy result = policyService.getPolicy(policyId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(policyId);
        assertThat(result.getPolicyHolderName()).isEqualTo("John Doe");

        verify(policyRepository).findById(policyId);
    }

    @Test
    @DisplayName("Should throw exception when policy not found")
    void shouldThrowExceptionWhenPolicyNotFound() {
        // Arrange
        String nonExistentId = "999999999999999999999999";
        when(policyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> policyService.getPolicy(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Policy with id " + nonExistentId + " not found");

        verify(policyRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should retrieve all policies successfully")
    void shouldRetrieveAllPoliciesSuccessfully() {
        // Arrange
        Policy policy2 = new Policy();
        policy2.setId("507f1f77bcf86cd799439012");
        policy2.setPolicyHolderName("Jane Smith");

        List<Policy> policies = Arrays.asList(validPolicy, policy2);
        when(policyRepository.findAll()).thenReturn(policies);

        // Act
        List<Policy> result = policyService.getAllPolicies();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPolicyHolderName()).isEqualTo("John Doe");
        assertThat(result.get(1).getPolicyHolderName()).isEqualTo("Jane Smith");

        verify(policyRepository).findAll();
    }

    // ========== Business Logic Tests ==========

    @Test
    @DisplayName("Should handle very large premium amounts")
    void shouldHandleVeryLargePremiumAmounts() {
        // Arrange
        Policy largePolicy = new Policy();
        largePolicy.setPolicyHolderName("John Doe");
        largePolicy.setPolicyType("Term Life");
        largePolicy.setBasePremium(1000000.0);  // Very large premium

        Policy savedLargePolicy = new Policy();
        savedLargePolicy.setId("507f1f77bcf86cd799439011");
        savedLargePolicy.setPolicyNumber("POL002");
        savedLargePolicy.setBasePremium(1000000.0);

        when(policyRepository.save(any(Policy.class))).thenReturn(savedLargePolicy);

        // Act
        Policy result = policyService.buyPolicy(largePolicy);

        // Assert
        assertThat(result.getBasePremium()).isEqualTo(1000000.0);
        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    @DisplayName("Should handle repository exceptions gracefully")
    void shouldHandleRepositoryExceptionsGracefully() {
        // Arrange
        Policy newPolicy = new Policy();
        newPolicy.setPolicyHolderName("John Doe");
        newPolicy.setBasePremium(1000.0);

        when(policyRepository.save(any(Policy.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThatThrownBy(() -> policyService.buyPolicy(newPolicy))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database connection failed");
    }
}