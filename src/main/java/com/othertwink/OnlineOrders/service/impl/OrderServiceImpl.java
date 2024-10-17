package com.othertwink.OnlineOrders.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.othertwink.OnlineOrders.model.Order;
import com.othertwink.OnlineOrders.repository.OrderRepo;
import com.othertwink.OnlineOrders.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderServiceImpl(OrderRepo orderRepo, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.orderRepo = orderRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        System.out.println("Created order: " + order.toString());
        sendOrderToKafka(order);
        return orderRepo.save(order);
    }

    private void sendOrderToKafka(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            kafkaTemplate.send("new_orders", orderJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error sending order to Kafka", e);
        }
    }

    @Override
    @Transactional
    public Order deleteOrder(Long id) {
        Order deleted = findById(id);
        orderRepo.delete(deleted);
        return deleted;
    }

    @Override
    @Transactional
    public Order findById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No order under Id " + id + " found"));
    }
}
