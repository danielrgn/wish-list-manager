package com.challenge.luizalabs.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
  public static class WishListProduct implements Serializable {
    private static final long serialVersionUID = -1812041212258478300L;

    private BigDecimal price;
    private String image;
    private String title;
    private float reviewScore;
    private String link;
  }
}
