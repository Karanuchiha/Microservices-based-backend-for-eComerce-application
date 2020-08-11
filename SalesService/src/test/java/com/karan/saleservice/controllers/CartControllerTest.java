package com.karan.saleservice.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.karan.saleservice.exceptions.SalesException;
import com.karan.saleservice.model.Cart;
import com.karan.saleservice.model.Order;
import com.karan.saleservice.model.Product;
import com.karan.saleservice.model.service.SalesRequestService;
import com.karan.saleservice.repositories.CartRepository;
import com.karan.saleservice.repositories.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class CartControllerTest {

  @Mock
  private ProcessInstance mockedProcessInstance;
  @Mock
  private CartRepository mockedCartRepository;
  @Mock
  private OrderRepository mockedOrderRepository;
  @Mock
  private SalesRequestService mockedSalesRequestService;
  @Mock
  private Environment mockedEnv;
  @Mock
  private RuntimeService mockedRuntimeService;
  @Mock
  private RestTemplate mockedRestTemplate;
  @InjectMocks
  private CartController cartController;
  private Long correctId;
  private Long wrongId;
  private List<Product> products = new ArrayList<>();
  private List<Product> emptyProductList = new ArrayList<>();
  private Product product1 = new Product("A001", 2);
  private Product product2 = new Product("A002", 5);
  private Cart cart;
  private Cart emptyCart;
  private Order order;

  @BeforeEach
  public void setUp() throws Exception {
    products.add(product1);
    products.add(product2);
    cart = new Cart(122L, "1", products, 120d, 240d);
    emptyCart = new Cart(128L, "1", emptyProductList, 120d, 240d);
    order = new Order(cart);
    wrongId = 34L;
    correctId = 12334L;
  }

  @Test
  public void testControllerReturnsHttpOkWhenCartCreated()
      throws SalesException {
    when(mockedSalesRequestService.requestService(
        anyString(), any(Cart.class), any(RestTemplate.class), anyString()))
        .thenReturn(true);
    when(mockedEnv.getProperty(anyString())).thenReturn("Test");
    ResponseEntity<Cart> response = cartController.createCart(cart, "any");
    Assertions.assertEquals(response.getStatusCode(), ResponseEntity.ok().build().getStatusCode());
  }

  @Test
  public void testControllerReturnsHttpBadRequestWhenProductORUserNotFound()
      throws SalesException {
    when(mockedSalesRequestService.requestService(
        anyString(), any(Cart.class), any(RestTemplate.class), anyString()))
        .thenReturn(false);
    when(mockedEnv.getProperty(anyString())).thenReturn("Test");
    ResponseEntity<Cart> response = cartController.createCart(cart, "any");
    Assertions.assertEquals(
        response.getStatusCode(), ResponseEntity.badRequest().build().getStatusCode());
  }

  @Test
  public void testControllerReturnsHttpOkWhenCartDeleted() {
    when(mockedCartRepository.findById(correctId)).thenReturn(Optional.of(cart));
    assertEquals(ResponseEntity.ok().build(), cartController.deleteCart(correctId));
  }

  @Test
  public void testControllerReturnsHttpBadRequestWhenCartNotDeleted() {
    when(mockedCartRepository.findById(wrongId)).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.badRequest().build(), cartController.deleteCart(wrongId));
  }

  @Test
  public void testControllerReturnsCartWhenCartSubmitted() {
    when(mockedCartRepository.findById(correctId)).thenReturn(Optional.of(cart));
    when(mockedOrderRepository.save(order)).thenReturn(order);
    when(mockedRestTemplate.exchange(
        anyString(), any(HttpMethod.class), any(), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(ResponseEntity.ok().build());
    when(mockedRuntimeService.startProcessInstanceByKey(anyString(), anyString()))
        .thenReturn(mockedProcessInstance);
    assertEquals(ResponseEntity.ok(order), cartController.submitCart(correctId));
  }

  @Test
  public void testControllerReturnsHttpNoContentWhenCartSubmittedNotFound() {
    when(mockedCartRepository.findById(wrongId)).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.noContent().build(), cartController.submitCart(wrongId));
  }

  @Test
  public void testControllerReturnsBadRequestWhenEmptyCartSubmitted() {
    when(mockedCartRepository.findById(correctId)).thenReturn(Optional.of(emptyCart));
    assertEquals(ResponseEntity.badRequest().build(), cartController.submitCart(correctId));
  }

  @Test
  public void testControllerReturnsCartWhenCartUpdated() throws SalesException {
    when(mockedCartRepository.findById(correctId)).thenReturn(Optional.ofNullable(cart));
    assertEquals(ResponseEntity.ok(cart), cartController.updateCart(correctId, cart, "any"));
  }

  @Test
  public void testControllerReturnsHTTPNoContentWhenCartUpdatedNotFound() throws SalesException {
    when(mockedCartRepository.findById(wrongId)).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.noContent().build(),
        cartController.updateCart(wrongId, cart, "any"));
  }
}
