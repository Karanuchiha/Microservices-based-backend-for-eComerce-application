package com.karan.saleservice.model.interfaces;

import com.karan.saleservice.exceptions.SalesException;

@FunctionalInterface
public interface ThrowingProduct<T, E extends Exception> {

  void accept(T t) throws E, SalesException;

}



