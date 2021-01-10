package com.challenge.luizalabs.v1.dto;

import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishListDto {

  private Long id;
  private Customer customer;
  private Product product;
}
