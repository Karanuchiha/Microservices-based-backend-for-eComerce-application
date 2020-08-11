package com.karan.notification;

import com.bearingpoint.common.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@EnableJms
public class NotificationSubscriber {

  private final Logger logger = LoggerFactory.getLogger(NotificationSubscriber.class);

  @JmsListener(
      destination = "order-topic",
      containerFactory = "jmsListenerContainerFactory",
      subscription = "notificationClientId")
  public void receiveTopicMessage(OrderDTO order) {

    logger.info("Message received {} ", order);
    logger.info("Message received successful");
  }
}
