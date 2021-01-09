package com.challenge.luizalabs.repository;

import com.challenge.luizalabs.model.Product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

}
