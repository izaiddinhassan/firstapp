package com.workshop.firstapp.repository;

import com.workshop.firstapp.model.Policy;
import com.workshop.firstapp.model.PolicyStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PolicyRepository extends MongoRepository<Policy, String> {

    // Basic derived queries
    Optional<Policy> findByPolicyNumber(String policyNumber);
    boolean existsByPolicyNumber(String policyNumber);
    List<Policy> findByPolicyType(String policyType);

    // Custom MongoDB queries using JSON syntax
    @Query("{'basePremium': {$gt: ?0}}")
    List<Policy> findExpensivePolicies(double minPremium);

    @Query("{'policyHolderName': {$regex: ?0, $options: 'i'}}")
    List<Policy> findByPolicyHolderNameContaining(String name);

    // Text search query
    @Query("{ $text: { $search: ?0 } }")
    List<Policy> searchByPolicyHolderName(String searchText);

    // Nested field queries using dot notation
    @Query("{'riders.name': {$in: ?0}}")
    List<Policy> findPoliciesWithRiders(List<String> riderNames);

    @Query("{'riders.active': true}")
    List<Policy> findPoliciesWithActiveRiders();

    // Aggregation query for policy statistics
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$policyType', 'count': { '$sum': 1 }, 'avgPremium': { '$avg': '$basePremium' } } }"
    })
    List<PolicyStats> getPolicyStatsByType();
}