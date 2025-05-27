package com.workshop.firstapp.controller;

import com.workshop.firstapp.model.Policy;
import com.workshop.firstapp.model.Rider;
import com.workshop.firstapp.model.PolicyStats;
import com.workshop.firstapp.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping("/buy")
    public ResponseEntity<Policy> buyPolicy(@RequestBody Policy policy) {
        return ResponseEntity.ok(policyService.buyPolicy(policy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getPolicy(@PathVariable String id) {
        return ResponseEntity.ok(policyService.getPolicy(id));
    }

    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Policy>> getAllPoliciesPaged(Pageable pageable) {
        return ResponseEntity.ok(policyService.getAllPolicies(pageable));
    }

    @PutMapping("/{id}/riders")
    public ResponseEntity<Policy> addRider(@PathVariable String id,
                                           @RequestBody Rider rider) {
        return ResponseEntity.ok(policyService.addRider(id, rider));
    }

    @DeleteMapping("/{id}/riders/{riderName}")
    public ResponseEntity<Policy> removeRider(@PathVariable String id,
                                              @PathVariable String riderName) {
        return ResponseEntity.ok(policyService.removeRider(id, riderName));
    }

    // New MongoDB-specific endpoints
    @GetMapping("/search")
    public ResponseEntity<List<Policy>> searchPolicies(
            @RequestParam String query) {
        return ResponseEntity.ok(policyService.searchPoliciesByHolderName(query));
    }

    @GetMapping("/expensive")
    public ResponseEntity<List<Policy>> getExpensivePolicies(
            @RequestParam double minPremium) {
        return ResponseEntity.ok(policyService.findExpensivePolicies(minPremium));
    }

    @GetMapping("/with-riders")
    public ResponseEntity<List<Policy>> getPoliciesWithRiders(
            @RequestParam List<String> riderNames) {
        return ResponseEntity.ok(policyService.findPoliciesWithRiders(riderNames));
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<PolicyStats>> getPolicyStatistics() {
        return ResponseEntity.ok(policyService.getPolicyStatistics());
    }
}