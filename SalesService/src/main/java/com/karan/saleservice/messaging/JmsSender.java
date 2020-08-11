package com.karan.saleservice.messaging;

import com.bearingpoint.common.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsSender {
  @Autowired private JmsTemplate jmsTemplate;

  @Value("${spring.activemq.orderTopic}")
  private String orderTopicName;

  public void sendMessage(OrderDTO orderToSend) {
    jmsTemplate.convertAndSend(orderTopicName, orderToSend);
  }
}
