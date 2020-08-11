package com.karan.saleservice.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.karan.saleservice.model.Cart;
import com.karan.saleservice.model.Order;
import com.karan.saleservice.model.Product;
import com.karan.saleservice.repositories.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class OrderControllerTest {

  @Mock
  private OrderRepository mockedOrderRepository;
  @InjectMocks
  private OrderController orderController;
  private Long correctId = 12334L;
  private Long wrongId = 34L;
  private List<Product> products = new ArrayList<>();
  private Product product1 = new Product("A001", 2);
  private Product product2 = new Product("A002", 5);
  private Order order;
  private String correctUserId = "correctId";
  private String wrongUserId = "wrongId";

  @BeforeEach
  public void setUp() throws Exception {
    products.add(product1);
    products.add(product2);
    Cart cart = new Cart(1L, "1", products, 120d, 240d);
    order = new Order(cart);
  }

  @Test
  public void testControllerReturnsOrderWhenOrderUpdatedFoundById() {
    when(mockedOrderRepository.findById(correctId))
        .thenReturn(java.util.Optional.ofNullable(order));
    assertEquals(ResponseEntity.ok(order), orderController.updateOrder(correctId, order));
  }

  @Test
  public void testControllerReturnsOptionalEmptyWhenOrderUpdatedNotFoundById() {
    when(mockedOrderRepository.findById(wrongId)).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.noContent().build(), orderController.updateOrder(wrongId, order));
  }

  @Test
  public void testControllerReturnsHttpOkWhenOrderDeletedFoundById() {
    when(mockedOrderRepository.findById(correctId)).thenReturn(java.util.Optional.of(order));
    assertEquals(ResponseEntity.ok().build(), orderController.deleteOrder(correctId));
  }

  @Test
  public void testControllerReturnsHttpBadRequestWhenOrderDeletedNotFoundById() {
    when(mockedOrderRepository.findById(wrongId)).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.badRequest().build(), orderController.deleteOrder(wrongId));
  }

  @Test
  public void testControllerReturnsOrderWhenDeliveredFoundById() {
    when(mockedOrderRepository.findById(correctId)).thenReturn(java.util.Optional.of(order));
    assertEquals(ResponseEntity.ok(order), orderController.setDelivered(correctId));
  }

  @Test
  public void testControllerReturnsOptionalEmptyWhenDeliveredNotFoundById() {
    when(mockedOrderRepository.findById(wrongId)).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.badRequest().build(), orderController.setDelivered(wrongId));
  }

  @Test
  public void testControllerReturnsOrderWhenOrdersNotCancelledFoundById() {
    when(mockedOrderRepository.findById(correctId)).thenReturn(Optional.of(order));
    assertEquals(ResponseEntity.ok(order), orderController.setCancelled(correctId));
  }

  @Test
  public void testControllerReturnsOptionalEmptyWhenOrdersNotCancelledFoundById() {
    when(mockedOrderRepository.findById(wrongId)).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.badRequest().build(), orderController.setCancelled(wrongId));
  }

  @Test
  public void testControllerReturnsOrderListWhenOrdersFoundByUserId() {
    List<Order> orders = new ArrayList<>();
    orders.add(order);
    when(mockedOrderRepository.findByUserId(correctUserId)).thenReturn(orders);
    assertEquals(ResponseEntity.ok(orders), orderController.getPreviousOrders(correctUserId));
  }

  @Test
  public void testControllerReturnsOrderListWhenOrdersNotFoundByUserId() {
    List<Order> emptyOrders = new ArrayList<>();
    when(mockedOrderRepository.findByUserId(wrongUserId)).thenReturn(emptyOrders);
    assertEquals(ResponseEntity.ok(emptyOrders), orderController.getPreviousOrders(wrongUserId));
  }

}
