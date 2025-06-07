package com.mock.restapi.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
            this.rootNode = objectMapper.readTree(inputStream).get(0);
        }
        catch (IOException e)
        {
            System.err.println("Error reading JSON file: " + jsonFilePath);
            throw e; // Re-throw to signal initialization failure
        }
    }

    public JsonNode getRootNode()
    {
        return rootNode;
    }

    public String getEndpoint()
    {
        if (rootNode == null)
        {
            return null;
        }
        return rootNode.get("endpoint").asText();
    }

    public String getMethods()
    {
        if (rootNode == null)
        {
            return null;
        }
        return rootNode.get("methods").asText();
    }

    public Map<String, String> getFields()
    {
        Map<String, String> fieldsMap = new HashMap<>();
        if (rootNode == null)
        {
            return null;
        }
        rootNode.get("fields")
                .fields()
                .forEachRemaining(entry -> fieldsMap.put(entry.getKey(),entry.getValue().asText()));

        return fieldsMap;
    }

    public Integer getCount()
    {
        if (rootNode == null)
        {
            return null;
        }
        return rootNode.get("count").asInt();
    }
}
