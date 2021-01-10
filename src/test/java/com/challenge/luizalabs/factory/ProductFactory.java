package com.challenge.luizalabs.factory;

import com.challenge.luizalabs.model.Product;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProductFactory extends AbstractFactory {

  @Override
  public Product simple() {
    return Product.builder()
        .price(BigDecimal.valueOf(0.0))
        .brand(RandomStringUtils.random(10, true, false))
        .title(RandomStringUtils.random(10, true, false))
        .image(RandomStringUtils.random(10, true, false))
        .reviewScore(0)
        .build();
  }
}
