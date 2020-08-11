package com.karan.saleservice.controllers;

import com.karan.saleservice.exceptions.SalesException;
import com.karan.saleservice.model.Cart;
import com.karan.saleservice.model.Order;
import com.karan.saleservice.model.Product;
import com.karan.saleservice.model.service.SalesRequestService;
import com.karan.saleservice.repositories.CartRepository;
import com.karan.saleservice.repositories.OrderRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/carts")
@Slf4j
public class CartController {

  @Autowired
  private SalesRequestService salesRequestService;
  @Autowired
  private CartRepository cartRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private Environment env;
  @Autowired
  private RuntimeService runtimeService;

  @PostMapping()
  @PreAuthorize("hasAuthority('Everyone')")
  public ResponseEntity<Cart> createCart(@Valid @RequestBody Cart cart,
      @RequestHeader(name = "Authorization") String token) throws SalesException {
    if (createValidCart(cart, token)) {
      updateProductsQuantity(cart);
      calculateTotalPrice(cart, token);
      return ResponseEntity.ok(cartRepository.save(cart));
    }
    return ResponseEntity.badRequest().build();
  }

  private boolean createValidCart(Cart cart, String token) throws SalesException {
    return salesRequestService.requestService(
        env.getProperty("gateway.address"), cart, restTemplate, token);
  }

  private void updateProductsQuantity(Cart cart) {
    List<Product> products = cart.getProducts();
    Map<String, Integer> productsMap =
        products.stream()
            .collect(Collectors.toMap(Product::getProductId, Product::getQuantity, Integer::sum));
    List<Product> updatedProducts =
        productsMap.entrySet().stream()
            .map(product -> new Product(product.getKey(), product.getValue()))
            .collect(Collectors.toList());
    cart.setProducts(updatedProducts);
  }

  private void calculateTotalPrice(Cart cart, String token) {
    double cartTotalPrice =
        salesRequestService
            .calculatePrice(env.getProperty("gateway.address"), cart, restTemplate, token);
    cart.setTotalPrice(cartTotalPrice);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('Everyone')")
  public ResponseEntity<Cart> updateCart(@PathVariable long id, @Valid @RequestBody Cart updateCart,
      @RequestHeader(name = "Authorization") String token)
      throws SalesException {
    Optional<Cart> currentCart = cartRepository.findById(id);
    if (currentCart.isPresent()) {
      Cart updatedCartToBeSaved = currentCart.get();
      cartRepository.delete(currentCart.get());
      if (createValidCart(updateCart, token)) {
        updateProductsQuantity(updateCart);
        calculateTotalPrice(updateCart, token);
        updatedCartToBeSaved.updateCart(updateCart);
        return ResponseEntity.ok(cartRepository.save(updatedCartToBeSaved));
      }
      return ResponseEntity.ok(updatedCartToBeSaved);
    }
    log.error("Id " + id + " does not exist");
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('Everyone')")
  public ResponseEntity<Cart> deleteCart(@PathVariable("id") long id) {
    Optional<Cart> persistedCart = cartRepository.findById(id);
    if (persistedCart.isPresent()) {
      cartRepository.delete(persistedCart.get());
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/{id}")
  @PreAuthorize("hasAuthority('Everyone')")
  public ResponseEntity<Order> submitCart(@PathVariable("id") long id) {
    ResponseEntity<Order> response = ResponseEntity.noContent().build();
    Optional<Cart> currentCart = cartRepository.findById(id);
    if (currentCart.isPresent()) {
      Cart cart = currentCart.get();
      if (cart.isValid()) {
        Order newOrder = new Order(cart);
        orderRepository.save(newOrder);
        deleteCart(cart.getId());

        VariableMap variables =
            Variables.createVariables()
                .putValueTyped("newOrder", Variables.objectValue(newOrder).serializationDataFormat(
                    Variables.SerializationDataFormats.JAVA).create());

        runtimeService.startProcessInstanceByKey("charge_payment", variables);
        response = ResponseEntity.ok(newOrder);
      } else {
        response = ResponseEntity.badRequest().build();
      }
    }
    return response;
  }
}
