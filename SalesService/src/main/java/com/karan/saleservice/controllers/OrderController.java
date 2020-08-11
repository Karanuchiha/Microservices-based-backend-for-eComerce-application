package com.karan.saleservice.controllers;

import com.karan.saleservice.model.Order;
import com.karan.saleservice.repositories.OrderRepository;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

  public static final String ID_S_DOES_NOT_EXIST = "Id %s does not exist";
  @Autowired
  private OrderRepository orderRepository;

  @PutMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('Everyone')")
  public ResponseEntity<Order> updateOrder(@PathVariable long id,
      @Valid @RequestBody Order updatedOrder) {
    Optional<Order> currentOrder = orderRepository.findById(id);

    if (currentOrder.isPresent()) {
      Order updatedOrderToBeSaved = currentOrder.get();
      updatedOrderToBeSaved.updateOrder(updatedOrder);
      orderRepository.save(updatedOrderToBeSaved);
      return ResponseEntity.ok(updatedOrderToBeSaved);
    }
    log.error(String.format(ID_S_DOES_NOT_EXIST, id));
    return ResponseEntity.noContent().build();
  }

  @PutMapping(value = "/{id}/delivered")
  @PreAuthorize("hasAuthority('Admin')")
  public ResponseEntity<Order> setDelivered(@PathVariable Long id) {
    Optional<Order> currentOrder = orderRepository.findById(id);
    if (currentOrder.isPresent()) {
      Order updatedOrderToBeSaved = currentOrder.get();
      updatedOrderToBeSaved.setStatus("delivered");
      orderRepository.save(updatedOrderToBeSaved);
      return ResponseEntity.ok(updatedOrderToBeSaved);
    }
    log.error(String.format(ID_S_DOES_NOT_EXIST, id));
    return ResponseEntity.badRequest().build();
  }

  @PutMapping(value = "/{id}/cancelled")
  public ResponseEntity<Order> setCancelled(@PathVariable Long id) {
    Optional<Order> currentOrder = orderRepository.findById(id);
    if (currentOrder.isPresent()) {
      Order updatedOrderToBeSaved = currentOrder.get();
      updatedOrderToBeSaved.setStatus("Cancelled");
      orderRepository.save(updatedOrderToBeSaved);
      return ResponseEntity.ok(updatedOrderToBeSaved);
    }
    log.error(String.format(ID_S_DOES_NOT_EXIST, id));
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('Everyone')")
  public ResponseEntity<Order> deleteOrder(@PathVariable("id") Long id) {
    Optional<Order> persistedorder = orderRepository.findById(id);
    if (orderRepository.findById(id).isPresent()) {
      orderRepository.delete(persistedorder.get());
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping(value = "/users/{userId}")
  @PreAuthorize("hasAuthority('Everyone')")public ResponseEntity<List<Order>> getPreviousOrders(@PathVariable("userId") String userId) {
    List<Order> previousOrders = orderRepository.findByUserId(userId);
    return ResponseEntity.ok(previousOrders);
  }
}
