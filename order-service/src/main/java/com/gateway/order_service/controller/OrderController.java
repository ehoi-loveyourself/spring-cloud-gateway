package com.gateway.order_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, String>> getOrder(@PathVariable String orderId) {
        Map<String, String> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("status", "shipped");

        return ResponseEntity.ok(order);
    }
}