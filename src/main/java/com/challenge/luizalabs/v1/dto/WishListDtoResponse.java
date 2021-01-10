package com.challenge.luizalabs.v1.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class WishListDtoResponse implements Serializable {
  private static final long serialVersionUID = -1812041212258478300L;

  private Long customerId;
  private List<WishListProduct> wishListProducts;

  @Builder
  public WishListDtoResponse(Long customerId, List<WishListProduct> wishListProducts) {
    this.customerId = customerId;
    this.wishListProducts = wishListProducts;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class WishListProduct implements Serializable {
    private static final long serialVersionUID = -1812041212258478300L;

    private BigDecimal price;
    private String image;
    private String title;
    private float reviewScore;
    private String link;
  }
}
