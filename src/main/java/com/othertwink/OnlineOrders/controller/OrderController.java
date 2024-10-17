package com.othertwink.OnlineOrders.controller;

import com.othertwink.OnlineOrders.model.Order;
import com.othertwink.OnlineOrders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
//@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ResponseEntity<Order> create (@RequestBody @Valid Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> list (@PathVariable Long id) {
        System.out.println("Endpoint list init: " + id);
        return ResponseEntity.ok(orderService.findById(id));
    }

}
