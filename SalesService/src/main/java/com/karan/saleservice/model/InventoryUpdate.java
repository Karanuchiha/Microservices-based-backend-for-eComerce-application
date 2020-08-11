package com.karan.saleservice.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class InventoryUpdate {

  private int quantity;
  private boolean addStock;

  public InventoryUpdate(@NotNull @Min(value = 0) int quantity, boolean addStock) {
    this.quantity = quantity;
    this.addStock = addStock;
  }
}
