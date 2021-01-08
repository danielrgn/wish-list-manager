package com.challenge.luizalabs.repository;

import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.model.WishList;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
  boolean existsWishListByCustomerIdAndProductId(Long customerId, UUID productId);
  void deleteByCustomerId(Long customerId);
  List<WishList> findAllByCustomerId(Long customerId);
}
