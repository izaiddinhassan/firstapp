package com.workshop.firstapp.service;

import com.workshop.firstapp.exception.ResourceNotFoundException;
import com.workshop.firstapp.model.Customer;
import com.workshop.firstapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    @Transactional(readOnly = true)
    public List<Customer> searchCustomers(String searchText) {
        return customerRepository.searchByName(searchText);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersByCity(String city) {
        return customerRepository.findByCity(city);
    }
}