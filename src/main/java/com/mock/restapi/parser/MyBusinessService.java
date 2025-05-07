package com.mock.restapi.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.mock.restapi.model.MockApiDefinition;
import com.mock.restapi.model.MockApiDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class responsible for processing and managing API endpoint configurations.
 * This service reads configuration from a JSON file at startup and maintains endpoint
 * metadata including available fields and their types.
 *
 * <p>The service expects a JSON configuration file with the following structure:
 * <pre>
 * {
 *   "endpoint": "string",     // API endpoint path
 *   "methods": "string",      // Supported HTTP methods
 *   "fields": {              // Field definitions
 *     "fieldName": "type",   // Each field and its corresponding data type
 *     ...
 *   }
 * }
 * </pre>
 *
 * @see ConfigFileParser
 * @since 1.0
 */
//TODO to rename class
@Service
public class MyBusinessService {
    private final ConfigFileParser configFileParser;

    /**
     * Constructs a new MyBusinessService with the required ConfigFileParser.
     *
     * @param configFileParser the service used to read and parse the configuration file
     */

    @Autowired
    public MyBusinessService(ConfigFileParser configFileParser) {
        this.configFileParser = configFileParser;
    }

    /**
     * Initializes the service by processing the configuration data at application startup.
     * This method validates and extracts essential configuration elements including:
     * - API endpoint information
     * - Supported HTTP methods
     * - Field definitions and their types
     *
     * @throws IllegalStateException if any required configuration element is missing
     */

    @PostConstruct
    private void processData() {
        JsonNode config = configFileParser.getRootNode().get(0);

        //Register the MockApiDefinition
        MockApiDefinitionRegistry mockApiDefinitionRegistry = MockApiDefinitionRegistry.getInstance();
        mockApiDefinitionRegistry.addDefinition(new MockApiDefinition(configFileParser.getEndpoint(), configFileParser.getFields(), configFileParser.getMethods(), configFileParser.getCount()));

        String endpointName = configFileParser.getEndpoint();
        String methodsType = configFileParser.getMethods();
        Map<String, String> fields = configFileParser.getFields();
        int count = configFileParser.getCount();

        // For debugging/verification
        System.out.println("Endpoint: " + endpointName);
        System.out.println("Methods: " + methodsType);
        System.out.println("Fields map: " + fields);
        System.out.println("Count: " + count);

    }
}