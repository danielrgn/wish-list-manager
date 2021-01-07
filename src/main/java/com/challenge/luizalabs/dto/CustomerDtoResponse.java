package com.challenge.luizalabs.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class CustomerDtoResponse {

  @NotNull
  private Long id;

  @NotNull
  private String name;

  @NotNull
  @Email
  private String email;
}
