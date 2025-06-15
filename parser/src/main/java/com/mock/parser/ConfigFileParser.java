package com.mock.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ConfigFileParser
{
    private JsonNode rootNode;

    @Value("${json.file.path:default.json}")
    private String jsonFilePath;

    private final ObjectMapper objectMapper;

    @Autowired
    public ConfigFileParser(ObjectMapper objectMapper)
    {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper must not be null");
    }

    @PostConstruct
    public void initializeRootNode() throws IOException
    {
        ClassPathResource resource = new ClassPathResource(jsonFilePath);
        try (InputStream inputStream = resource.getInputStream())
        {
            this.rootNode = objectMapper.readTree(inputStream);
        }
        catch (IOException e)
        {
            System.err.println("Error reading JSON file: " + jsonFilePath);
            throw e;
        }
    }
    
    public List<JsonNode> getAllEndpoints()
    {
        if (rootNode == null)
        {
            return null;
        }
        else
        {
            List<JsonNode> endpoints = new ArrayList<>();
            rootNode.forEach(endpoints::add);
            return endpoints;
        }
    }

    public String getName(JsonNode node)
    {
        return node.get("name").asText();
    }

    public String getEndpoint(JsonNode node)
    {
        if (node == null)
        {
            return null;
        }
        return node.get("endpoint").asText();
    }

    public String getMethods(JsonNode node)
    {
        if (node == null)
        {
            return null;
        }
        return node.get("methods").asText();
    }

    public Map<String, String> getFields(JsonNode node)
    {
        Map<String, String> fieldsMap = new HashMap<>();
        if (node == null)
        {
            return null;
        }
        node.get("fields")
                .fields()
                .forEachRemaining(entry -> fieldsMap.put(entry.getKey(),entry.getValue().asText()));

        return fieldsMap;
    }

    public Integer getCount(JsonNode node)
    {
        if (node == null)
        {
            return null;
        }
        return node.get("count").asInt();
    }
}
