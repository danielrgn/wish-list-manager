package com.challenge.luizalabs.dto;

import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.model.Product;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
