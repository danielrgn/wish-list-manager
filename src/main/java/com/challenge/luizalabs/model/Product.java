package com.challenge.luizalabs.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

  @Id
  @Type(type = "uuid-char")
  private UUID id;

  @NotNull
  @Column
  private String title;

  @Column
  private String brand;

  @Column
  private BigDecimal price;

  @Column
  private String image;

  @Column
  private float reviewScore;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
  private List<WishList> wishLists;
}
