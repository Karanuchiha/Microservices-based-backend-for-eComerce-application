package com.karan.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InventoryUpdate {

    boolean addStock;
    int stock;
}
