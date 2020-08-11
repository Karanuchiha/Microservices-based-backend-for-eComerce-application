package com.karan.saleservice.camunda;

import com.bearingpoint.common.OrderDTO;
import com.karan.saleservice.messaging.JmsSender;
import com.karan.saleservice.messaging.OrderMapper;
import com.karan.saleservice.model.Order;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PayForOrder implements JavaDelegate {
  @Autowired
  private JmsSender jmsNewOrder;

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    Order newOrder = (Order) execution.getVariable("newOrder");

    OrderDTO orderDto = OrderMapper.INSTANCE.toDto(newOrder);
    log.info("Order DTO: " + orderDto);
    jmsNewOrder.sendMessage(orderDto);

    Random random = new Random();
    log.info("Processing request ");
    execution.setVariable("pay", random.nextBoolean());
  }
}
