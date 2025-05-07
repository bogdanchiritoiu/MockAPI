package com.mock.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mocks")
public class MockApiController
{
    @RequestMapping("/{any}/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String id, @PathVariable String any)
    {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("structure", any);
        user.put("name", "John Doe");
        user.put("email", "john.doe@example.com");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Map<String, Object> productData)
    {
        // In a real scenario, you might save this data.
        // For a mock, we can just echo it back with a success message.
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product created successfully");
        response.put("data", productData);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<String> updateItem(@PathVariable String itemId, @RequestBody Map<String, Object> updateData)
    {
        return ResponseEntity.ok("Item with ID " + itemId + " updated successfully.");
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId)
    {
        return ResponseEntity.noContent().build();
    }
}
