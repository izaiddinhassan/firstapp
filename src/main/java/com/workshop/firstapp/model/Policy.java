package com.workshop.firstapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {
    @Id
    private String id; // Changed from Long to String for MongoDB

    @Indexed(unique = true)
    private String policyNumber;

    @TextIndexed // Enable text search on policy holder name
    private String policyHolderName;

    private String policyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private double basePremium;
    private double sumAssured;

    // Embedded approach - riders stored within policy document
    private List<Rider> riders = new ArrayList<>();

    // Reference approach - customer in separate collection
    @DBRef
    private Customer customer;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Helper methods for managing embedded riders
    public void addRider(Rider rider) {
        riders.add(rider);
        this.basePremium += rider.getPremium();
    }

    public void removeRider(Rider rider) {
        riders.remove(rider);
        this.basePremium -= rider.getPremium();
    }
}