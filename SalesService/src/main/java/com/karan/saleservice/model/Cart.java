package com.karan.saleservice.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "carts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @NotEmpty
  @Column(name = "userId")
  private String userId;


  @ManyToMany(
      cascade = CascadeType.ALL
  )
  @Column(name = "products")
  private List<Product> products;

  @NotNull
  @Min(value = 0)
  @Column(name = "deliveryPrice")
  private Double deliveryPrice;

  @NotNull
  @Min(value = 0)
  @Column(name = "totalPrice")
  private Double totalPrice;

  public void updateCart(Cart updatedCart) {
    this.setId(updatedCart.getId());
    this.setUserId(updatedCart.getUserId());
    this.setDeliveryPrice(updatedCart.getDeliveryPrice());
    this.setProducts(updatedCart.getProducts());
    this.setTotalPrice(updatedCart.getTotalPrice());
  }


  public boolean isValid() {
    return this.getProducts().size() > 0;
  }

}
