package com.karan.saleservice.model.service;

import com.karan.saleservice.exceptions.SalesException;
import com.karan.saleservice.model.Cart;
import com.karan.saleservice.model.Product;
import java.util.List;

import com.karan.saleservice.model.implementations.SalesWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


@Slf4j
public class SalesRequestService {

  public boolean requestService(String gateway, Cart cart, RestTemplate restTemplate, String token)
      throws SalesException {
    boolean isValidCart = false;
    List<Product> products = cart.getProducts();
    ResponseEntity<String> validUserResponse = getEntity(gateway, "/users/", cart.getUserId(),
        restTemplate, token);

    if (validUserResponse.getStatusCode().is2xxSuccessful()) {
      products.forEach(SalesWrapper.throwingConsumerWrapper(p ->
          getEntity(gateway, "/products/", p.getProductId(),
              restTemplate, token)));
      isValidCart = true;
    }
    return isValidCart;
  }

  public double calculatePrice(String gateway, Cart cart, RestTemplate restTemplate, String token) {
    double totalPrice;
    List<Product> products = cart.getProducts();
    totalPrice = products.stream().mapToDouble(
        product -> product.getQuantity() * requestProductPrice(gateway, product.getProductId(),
            restTemplate, token)).sum();
    return totalPrice;
  }

  private double requestProductPrice(String gateway, String productId, RestTemplate restTemplate,
      String token) {
    double productPrice;
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);
    HttpEntity entity = new HttpEntity(headers);
    ResponseEntity<Double> validProductPrice = restTemplate
        .exchange(gateway + "/products/price/" + productId, HttpMethod.GET, entity, Double.class);
    productPrice = validProductPrice.getBody();
    return productPrice;
  }


  public ResponseEntity<String> getEntity(String gateway, String microservice, String id,
      RestTemplate restTemplate, String token) throws SalesException {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);
    HttpEntity entity = new HttpEntity(headers);
    try {
      return restTemplate
          .exchange(gateway + microservice + id, HttpMethod.GET, entity, String.class);
    } catch (ResourceAccessException e) {
      throw new SalesException(
          HttpStatus.BAD_REQUEST,
          String.format("%s microservice or Gateway is down", microservice.replace("/", "")), e);
    } catch (HttpClientErrorException e) {
      throw new SalesException(
          String.format("%s id %s is not a valid ID", microservice.replace("/", ""), id));
    }

  }
}
