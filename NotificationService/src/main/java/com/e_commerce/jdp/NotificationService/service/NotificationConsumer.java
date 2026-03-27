package com.e_commerce.jdp.NotificationService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private SimpMessagingTemplate template;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consume(String message) {
        logger.info("Consumed notification message from Kafka: {}", message);
        // Push to WebSocket topic
        template.convertAndSend("/topic/notifications", message);
    }
}
