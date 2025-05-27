package com.workshop.firstapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyStats {
    private String policyType;  // Maps to '_id' from aggregation
    private Long count;
    private Double avgPremium;
}