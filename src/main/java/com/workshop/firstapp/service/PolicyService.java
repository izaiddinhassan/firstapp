package com.workshop.firstapp.service;

import com.workshop.firstapp.exception.ResourceNotFoundException;
import com.workshop.firstapp.model.Policy;
import com.workshop.firstapp.model.Rider;
import com.workshop.firstapp.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional // All methods in this service run within a transaction
public class PolicyService {

    private final PolicyRepository policyRepository;

    public Policy buyPolicy(Policy policy) {
        // Generate policy number if not provided
        if (policy.getPolicyNumber() == null || policy.getPolicyNumber().isEmpty()) {
            policy.setPolicyNumber(generatePolicyNumber());
        }

        return policyRepository.save(policy);
    }

    @Transactional(readOnly = true) // Read-only transaction for query operations
    public Policy getPolicy(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", id.toString()));
    }

    @Transactional(readOnly = true)
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Policy> getAllPolicies(Pageable pageable) {
        return policyRepository.findAll(pageable);
    }

    public Policy addRider(Long policyId, Rider rider) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId.toString()));

        rider.setPolicy(policy);
        policy.getRiders().add(rider);
        policy.setBasePremium(policy.getBasePremium() + rider.getPremium());

        return policyRepository.save(policy);
    }

    public Policy deleteRider(Long policyId, Long riderId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId.toString()));

        boolean riderRemoved = policy.getRiders().removeIf(r -> {
            if (r.getId().equals(riderId)) {
                policy.setBasePremium(policy.getBasePremium() - r.getPremium());
                return true;
            }
            return false;
        });

        if (!riderRemoved) {
            throw new ResourceNotFoundException("Rider", riderId.toString());
        }

        return policyRepository.save(policy);
    }

    // Helper method to generate policy number
    private String generatePolicyNumber() {
        long count = policyRepository.count();
        return "POL" + String.format("%06d", count + 1);
    }
}