package com.workshop.firstapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "riders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name; // e.g., "Accidental Death Benefit", "Critical Illness"

    @Column(name = "premium", nullable = false)
    private double premium; // Additional cost

    @Column(name = "coverage_amount", nullable = false)
    private double coverageAmount; // Coverage this rider provides

    @Column(name = "term_in_years", nullable = false)
    private int termInYears; // Validity period for rider

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    @JsonIgnore // Prevent infinite recursion in JSON serialization
    private Policy policy;
}
