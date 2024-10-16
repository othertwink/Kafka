package com.othertwink.shipping.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.othertwink.shipping.model.dto.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payed_orders", groupId = "shipping_group")
    public void processPayment(String message) {
        try {

            Order order = objectMapper.readValue(message, Order.class);
            // типа об
            order.setStatus("SHIPPED");

            sendShippedOrderToKafka(order);

            System.out.println("Shipping processed for order: " + message);

        } catch (Exception e) {
            System.err.println("Error processing shipping: " + e.getMessage());
        }
    }

    private void sendShippedOrderToKafka(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            kafkaTemplate.send("sent_orders", orderJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error sending shipped order to Kafka", e);
        }
    }
}

