package com.workshop.firstapp.controller;

import com.workshop.firstapp.model.Policy;
import com.workshop.firstapp.model.Rider;
import com.workshop.firstapp.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public ResponseEntity<Policy> getPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicy(id));
    }

    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        List<Policy> policies = policyService.getAllPolicies();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Policy>> getAllPoliciesPaged(Pageable pageable) {
        return ResponseEntity.ok(policyService.getAllPolicies(pageable));
    }

    @PutMapping("/{id}/riders")
    public ResponseEntity<Policy> addRider(@PathVariable Long id, @RequestBody Rider rider) {
        return ResponseEntity.ok(policyService.addRider(id, rider));
    }

    @DeleteMapping("/{id}/riders/{riderId}")
    public ResponseEntity<Policy> deleteRider(@PathVariable Long id, @PathVariable Long riderId) {
        return ResponseEntity.ok(policyService.deleteRider(id, riderId));
    }
}