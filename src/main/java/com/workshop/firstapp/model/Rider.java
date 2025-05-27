package com.workshop.firstapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rider {
    // No @Id annotation - this is an embedded document
    private String name;
    private String description;
    private double premium;
    private double coverageAmount;
    private int termInYears;
    private boolean active;
}