package com.challenge.luizalabs.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CustomerDtoRequest {

  @NotNull(message = "The field name can't be null")
  private String name;

  @NotNull(message = "The field email can't be null")
  @Email
  private String email;
}
