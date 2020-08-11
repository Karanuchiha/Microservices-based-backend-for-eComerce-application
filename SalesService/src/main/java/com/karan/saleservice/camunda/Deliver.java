package com.karan.saleservice.camunda;

import com.bearingpoint.common.OrderDTO;
import com.karan.saleservice.controllers.OrderController;
import com.karan.saleservice.messaging.JmsSender;
import com.karan.saleservice.messaging.OrderMapper;
import com.karan.saleservice.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Deliver implements JavaDelegate {

  @Autowired
  private OrderController orderController;
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private JmsSender jmsStatusChanged;

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    Order newOrder = (Order) execution.getVariable("newOrder");

    log.info("Processing request by " + newOrder.getId());
    log.info("Delivered");
    runtimeService.setVariable(execution.getId(), "status", "delivered");
    orderController.setDelivered(newOrder.getId());

    OrderDTO orderDto = OrderMapper.INSTANCE.toDto(newOrder);
    jmsStatusChanged.sendMessage(orderDto);
  }
}
