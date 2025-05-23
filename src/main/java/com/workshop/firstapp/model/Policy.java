package com.workshop.firstapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", unique = true, nullable = false)
    private String policyNumber;

    @Column(name = "policy_holder_name", nullable = false)
    private String policyHolderName;

    @Column(name = "policy_type", nullable = false)
    private String policyType; // e.g., "Term", "Whole Life", "Universal"

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "base_premium", nullable = false)
    private double basePremium;

    @Column(name = "sum_assured", nullable = false)
    private double sumAssured;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Rider> riders = new ArrayList<>();

    // Helper method for managing bidirectional relationship
    public void addRider(Rider rider) {
        riders.add(rider);
        rider.setPolicy(this);
        this.basePremium += rider.getPremium();
    }

    public void removeRider(Rider rider) {
        riders.remove(rider);
        rider.setPolicy(null);
        this.basePremium -= rider.getPremium();
    }
}