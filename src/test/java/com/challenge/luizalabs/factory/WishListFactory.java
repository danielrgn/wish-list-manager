package com.challenge.luizalabs.factory;

import com.challenge.luizalabs.model.WishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListFactory extends AbstractFactory {

  @Autowired
  private CustomerFactory customerFactory;

  @Autowired
  private ProductFactory productFactory;

  @Override
  public WishList simple() {
    return WishList.builder()
        .customer(customerFactory.simple())
        .product(productFactory.simple())
        .build();
  }
}
