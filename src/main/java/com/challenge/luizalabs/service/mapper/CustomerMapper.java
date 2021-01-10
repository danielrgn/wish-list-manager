package com.challenge.luizalabs.service.mapper;

import com.challenge.luizalabs.model.Customer;
import com.challenge.luizalabs.v1.dto.CustomerDtoResponse;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  /**
   * Static method responsible to serialize the input param to another class output.
   *
   * @param customer {Customer}
   * @return {CustomerDtoResponse}
   */
  public static CustomerDtoResponse serialize(final @NonNull Customer customer) {
    return CustomerDtoResponse.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .build();
  }
}
