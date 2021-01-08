package com.challenge.luizalabs.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

  private UUID id;
  private BigDecimal price;
  private String image;
  private String brand;
  private String title;
  private float reviewScore;
}
