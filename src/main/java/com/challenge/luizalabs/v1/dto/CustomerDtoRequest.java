package com.challenge.luizalabs.v1.dto;

import java.io.Serializable;
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
public class CustomerDtoRequest implements Serializable {
  private static final long serialVersionUID = 2520333980388183439L;

  @NotNull(message = "The field name can't be null")
  private String name;

  @NotNull(message = "The field email can't be null")
  @Email
  private String email;
}
