package com.mock.api.register;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Component
public class DynamicRequestHandler
{
    @ResponseBody
    public ResponseEntity<String> handleGet()
    {
        return ResponseEntity.ok("GET all resources");
    }

    @ResponseBody
    public ResponseEntity<String> handleGet(@PathVariable String id)
    {
        return ResponseEntity.ok("GET request processed for ID: " + id);
    }

    @ResponseBody
    public ResponseEntity<String> handlePost(@RequestBody Map<String, Object> requestBody)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body("Resource created successfully");
    }

    @ResponseBody
    public ResponseEntity<String> handlePut(
            @PathVariable String id,
            @RequestBody Map<String, Object> requestBody)
    {
        return ResponseEntity.ok("Resource " + id + " updated successfully");
    }

    @ResponseBody
    public ResponseEntity<String> handleDelete(@PathVariable String id)
    {
        return ResponseEntity.ok("Resource " + id + " deleted successfully");
    }
}
