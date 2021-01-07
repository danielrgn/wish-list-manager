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

  @NotNull
  private String name;

  @NotNull
  @Email
  private String email;
}
