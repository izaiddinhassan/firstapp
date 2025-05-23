package com.workshop.firstapp.repository;

import com.workshop.firstapp.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    // Find riders by policy ID
    List<Rider> findByPolicyId(Long policyId);
}
