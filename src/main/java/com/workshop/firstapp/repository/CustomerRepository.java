package com.workshop.firstapp.repository;

import com.workshop.firstapp.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByEmail(String email);

    // Text search on customer name
    @Query("{ $text: { $search: ?0 } }")
    List<Customer> searchByName(String searchText);

    // Address queries using dot notation
    @Query("{'address.city': ?0}")
    List<Customer> findByCity(String city);

    @Query("{'address.state': ?0}")
    List<Customer> findByState(String state);
}