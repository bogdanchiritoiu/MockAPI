package com.mock.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.mock.model.MockApiDefinition;
import com.mock.model.MockApiDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final Logger LOG = LogManager.getLogger();

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
    public void processData()
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

            LOG.debug("Registered endpoint: {}", configFileParser.getName(config));
            LOG.debug("Endpoint: {}", configFileParser.getEndpoint(config));
            LOG.debug("Methods: {}", configFileParser.getMethods(config));
            LOG.debug("Fields: {}", configFileParser.getFields(config));
            LOG.debug("Count: {}", configFileParser.getCount(config));
        }
    }

    // Add a method to get the registry for testing
    MockApiDefinitionRegistry getRegistry()
    {
        return MockApiDefinitionRegistry.getInstance();
    }

    public ConfigFileParser getConfigFileParser()
    {
        return configFileParser;
    }
}