package com.challenge.luizalabs.dto;

import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WishListDto {

  private Long id;
  private Customer customer;
  private Product product;
}
