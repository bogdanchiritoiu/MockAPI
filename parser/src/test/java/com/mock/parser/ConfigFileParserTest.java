package com.mock.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;


class ConfigFileParserTest
{
    private ConfigFileParser configFileParser;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_JSON_PATH = "test-data.json";

    @BeforeEach
    void setUp() throws IOException
    {
        configFileParser = new ConfigFileParser(objectMapper, TEST_JSON_PATH);
        configFileParser.initializeRootNode(); // We need to call this explicitly in tests
    }

    @Test
    void constructorTest()
    {
        assertNotNull(configFileParser);
        assertNotNull(configFileParser.getAllEndpoints());
        assertEquals(2, configFileParser.getAllEndpoints().size());
    }

    @Test
    void constructWithNullJsonFilePath()
    {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new ConfigFileParser(objectMapper, null));
        String exMessage = exception.getMessage();

        String expectedMessage = "JSON file path must not be null";
        assertEquals(expectedMessage, exMessage);
    }

    @Test
    void constructWithNullObjectMapper()
    {
        Exception exception = assertThrows(NullPointerException.class, () ->
                new ConfigFileParser(null, anyString()));

        String exMessage = exception.getMessage();
        String expectedMessage = "ObjectMapper must not be null";
        assertEquals(expectedMessage, exMessage);
    }

    @Test
    void checkRootNodeWithErrors()
    {
        configFileParser = new ConfigFileParser(objectMapper, "wrong.json");
        assertNull(configFileParser.getAllEndpoints());
    }

    @Test
    void checkGetMethods()
    {
        List<JsonNode> allEndpoints = configFileParser.getAllEndpoints();

        String endpoint1 = configFileParser.getEndpoint(allEndpoints.get(0));
        String endpoint2 = configFileParser.getEndpoint(allEndpoints.get(1));

        String name1 = configFileParser.getName(allEndpoints.get(0));
        String name2 = configFileParser.getName(allEndpoints.get(1));

        String methods1 = configFileParser.getMethods(allEndpoints.get(0));
        String methods2 = configFileParser.getMethods(allEndpoints.get(1));

        Map<String, String> fields1 = configFileParser.getFields(allEndpoints.get(0));
        Map<String, String> fields2 = configFileParser.getFields(allEndpoints.get(1));

        String count1 = configFileParser.getCount(allEndpoints.get(0)).toString();
        String count2 = configFileParser.getCount(allEndpoints.get(1)).toString();

        assertNotNull(allEndpoints);

        assertNotNull(endpoint1);
        assertEquals( "/api/users", endpoint1);
        assertNotNull(endpoint2);
        assertEquals( "/api/products", endpoint2);

        assertNotNull(name1);
        assertEquals( "users", name1);
        assertNotNull(name2);
        assertEquals( "products", name2);

        assertNotNull(methods1);
        assertEquals( "GET,POST", methods1);
        assertNotNull(methods2);
        assertEquals( "GET", methods2);

        assertNotNull(fields1);
        assertEquals( 3, fields1.size());
        assertNotNull(fields2);
        assertEquals( 3, fields2.size());

        assertNotNull(count1);
        assertEquals( "10", count1);
        assertNotNull(count2);
        assertEquals("5", count2);

        assertEquals(2, allEndpoints.size());
    }

}