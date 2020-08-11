package com.karan.saleservice.model;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @NotEmpty
  @Column(name = "userId")
  private String userId;

  @NotEmpty
  @Column(name = "paymentReference")
  private String paymentReference;

  @Column(name = "status")
  private String status;

  @ManyToMany(
      cascade = CascadeType.ALL
  )
  @Column(name = "order_products")
  private List<Product> products;

  @NotNull
  @Min(value = 0)
  @Column(name = "totalPrice")
  private Double totalPrice;

  public Order(Cart cart) {

    this.userId = cart.getUserId();
    this.paymentReference = "QWERTY";
    this.status = "payment pending";
    this.products = new ArrayList<>();
    cart.getProducts().forEach(product -> {
      this.products.add(new Product(product.getProductId(), product.getQuantity()));
    });
    this.totalPrice = cart.getTotalPrice();
  }

  public void updateOrder(Order updatedOrder) {
    this.setId(updatedOrder.getId());
    this.setPaymentReference(updatedOrder.getPaymentReference());
    this.setUserId(updatedOrder.getUserId());
    this.setStatus(updatedOrder.getStatus());
    this.setProducts(updatedOrder.getProducts());
    this.setTotalPrice(updatedOrder.getTotalPrice());
  }
}
