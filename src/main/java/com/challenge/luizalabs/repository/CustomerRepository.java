package com.challenge.luizalabs.repository;

import com.challenge.luizalabs.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Customer findByEmail(String email);
}
