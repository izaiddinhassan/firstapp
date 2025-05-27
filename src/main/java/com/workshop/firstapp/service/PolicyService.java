package com.workshop.firstapp.service;

import com.workshop.firstapp.exception.ResourceNotFoundException;
import com.workshop.firstapp.model.Policy;
import com.workshop.firstapp.model.Rider;
import com.workshop.firstapp.model.PolicyStats;
import com.workshop.firstapp.repository.PolicyRepository;
import com.workshop.firstapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional // MongoDB supports transactions in replica sets
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;

    public Policy buyPolicy(Policy policy) {
        // Generate policy number if not provided
        if (policy.getPolicyNumber() == null || policy.getPolicyNumber().isEmpty()) {
            policy.setPolicyNumber(generatePolicyNumber());
        }
        return policyRepository.save(policy);
    }

    @Transactional(readOnly = true)
    public Policy getPolicy(String id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", id));
    }

    @Transactional(readOnly = true)
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Policy> getAllPolicies(Pageable pageable) {
        return policyRepository.findAll(pageable);
    }

    public Policy addRider(String policyId, Rider rider) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId));

        rider.setActive(true);
        policy.addRider(rider); // Uses helper method
        return policyRepository.save(policy);
    }

    public Policy removeRider(String policyId, String riderName) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId));

        boolean riderRemoved = policy.getRiders().removeIf(r -> {
            if (r.getName().equals(riderName)) {
                policy.setBasePremium(policy.getBasePremium() - r.getPremium());
                return true;
            }
            return false;
        });

        if (!riderRemoved) {
            throw new ResourceNotFoundException("Rider", riderName);
        }

        return policyRepository.save(policy);
    }

    // New MongoDB-specific methods
    @Transactional(readOnly = true)
    public List<Policy> searchPoliciesByHolderName(String searchText) {
        return policyRepository.searchByPolicyHolderName(searchText);
    }

    @Transactional(readOnly = true)
    public List<Policy> findExpensivePolicies(double minPremium) {
        return policyRepository.findExpensivePolicies(minPremium);
    }

    @Transactional(readOnly = true)
    public List<Policy> findPoliciesWithRiders(List<String> riderNames) {
        return policyRepository.findPoliciesWithRiders(riderNames);
    }

    @Transactional(readOnly = true)
    public List<PolicyStats> getPolicyStatistics() {
        return policyRepository.getPolicyStatsByType();
    }

    private String generatePolicyNumber() {
        long count = policyRepository.count();
        return "POL" + String.format("%06d", count + 1);
    }
}