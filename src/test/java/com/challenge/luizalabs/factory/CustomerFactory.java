package com.challenge.luizalabs.factory;

import com.challenge.luizalabs.model.Customer;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerFactory extends AbstractFactory {

  @Override
  public Customer simple() {
    return Customer.builder()
        .name(RandomStringUtils.random(10, true, false))
        .email(RandomStringUtils.random(10, true, false))
        .build();
  }
}
