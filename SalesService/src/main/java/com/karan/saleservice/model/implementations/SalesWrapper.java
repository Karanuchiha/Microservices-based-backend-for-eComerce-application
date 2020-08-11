package com.karan.saleservice.model.implementations;

import com.karan.saleservice.exceptions.SalesException;
import com.karan.saleservice.model.interfaces.ThrowingProduct;

import java.util.function.Consumer;

public class SalesWrapper implements ThrowingProduct {

  public static <T> Consumer<T> throwingConsumerWrapper(
      ThrowingProduct<T, Exception> throwingProduct) {

    return i -> {
      try {
        throwingProduct.accept(i);
      } catch (Exception | SalesException ex) {
        throw new RuntimeException(ex);
      }
    };
  }

  @Override
  public void accept(Object o) {
  }
}
