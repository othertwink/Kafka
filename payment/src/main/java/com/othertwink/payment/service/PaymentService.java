package com.othertwink.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.othertwink.payment.model.dto.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // КАК ПРОКИДЫВАТЬ enum из другого приложения??
    @KafkaListener(topics = "new_orders", groupId = "payment_group")
    public void processOrder(String message) {
        try {
            Order order = objectMapper.readValue(message, Order.class);

            // типа обработали платёж
            order.setStatus("PAYED");

            sendPayedOrderToKafka(order);

            System.out.println("Payment processed for order: " + message);

        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
        }
    }

    private void sendPayedOrderToKafka(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            kafkaTemplate.send("payed_orders", orderJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error sending payed order to Kafka", e);
        }
    }
}
