package com.gateway.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, String>> getUser(@PathVariable String userId) {
        Map<String, String> user = new HashMap<>();
        user.put("userId", userId);
        user.put("name", "Taeyi Park");
        return ResponseEntity.ok(user);
    }
}