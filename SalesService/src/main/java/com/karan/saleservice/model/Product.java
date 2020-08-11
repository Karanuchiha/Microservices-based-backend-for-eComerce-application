package com.karan.saleservice.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Entity
@Table(name = "products")

@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  public Product(@NotEmpty String productId,
      @NotNull @Min(value = 0) int quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  @NotEmpty
  private String productId;

  @NotNull
  @Min(value = 0)
  private int quantity;


}
