package com.challenge.luizalabs.factory;

import com.challenge.luizalabs.v1.dto.CustomerDtoRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerDtoRequestFactory extends AbstractFactory {

  @Override
  public CustomerDtoRequest simple() {
    return CustomerDtoRequest.builder()
        .name(RandomStringUtils.random(10, true, false))
        .email(RandomStringUtils.random(10, true, false).concat("@teste.com.br"))
        .build();
  }
}
