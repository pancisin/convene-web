package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
