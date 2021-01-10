package com.challenge.luizalabs.v1.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDtoResponse {

  @NotNull
  private Long id;

  @NotNull
  private String name;

  @NotNull
  @Email
  private String email;
}
