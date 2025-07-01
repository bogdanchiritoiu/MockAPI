package com.mock.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.mock.model.MockApiDefinition;
import com.mock.model.MockApiDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class ApiConfigurationService
{
    private final ConfigFileParser configFileParser;

    /**
     * Constructs a new MyBusinessService with the required com.mock.parser.ConfigFileParser.
     *
     * @param configFileParser the service used to read and parse the configuration file
     */

    @Autowired
    public ApiConfigurationService(ConfigFileParser configFileParser)
    {
        this.configFileParser = configFileParser;
    }

    /**
     * Initializes the service by processing the configuration data at application startup.
     * This method validates and extracts essential configuration elements including
     * - API endpoint information
     * - Supported HTTP methods
     * - Field definitions and their types
     *
     * @throws IllegalStateException if any required configuration element is missing
     */

    @PostConstruct
    private void processData()
    {
        for (JsonNode config : configFileParser.getAllEndpoints())
        {
            //Register the com.mock.model.MockApiDefinition
            MockApiDefinitionRegistry mockApiDefinitionRegistry = MockApiDefinitionRegistry.getInstance();
            mockApiDefinitionRegistry.addDefinition(new MockApiDefinition(configFileParser.getName(config),
                                                                        configFileParser.getEndpoint(config),
                                                                        configFileParser.getFields(config),
                                                                        configFileParser.getMethods(config),
                                                                        configFileParser.getCount(config)));

            /* Only for debug */
            String name = configFileParser.getName(config);
            String endpointName = configFileParser.getEndpoint(config);
            String methodsType = configFileParser.getMethods(config);
            Map<String, String> fields = configFileParser.getFields(config);
            int count = configFileParser.getCount(config);

            // For debugging/verification
            System.out.println("Name: " + name);
            System.out.println("Endpoint: " + endpointName);
            System.out.println("Methods: " + methodsType);
            System.out.println("Fields map: " + fields);
            System.out.println("Count: " + count);
        }

    }
}