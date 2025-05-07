package com.mock.restapi.model;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//TODO add java doc and comments
@Service
public class MockApiDefinitionRegistry
{
    private static MockApiDefinitionRegistry instance;
    private final List<MockApiDefinition> definitions = new CopyOnWriteArrayList<>();

    @PostConstruct
    private void initialize()
    {
        instance = this;
    }

    public static MockApiDefinitionRegistry getInstance()
    {
        return instance;
    }

    public void addDefinition(MockApiDefinition definition)
    {
        definitions.add(definition);
    }

    public List<MockApiDefinition> getDefinitions()
    {
        return Collections.unmodifiableList(definitions);
    }

    public MockApiDefinition getDefinitionByEndpoint(String endpointName)
    {
        return definitions.stream()
                .filter(def -> def.getEndpointName().equals(endpointName))
                .findFirst()
                .orElse(null);
    }

    public void clear() {
        definitions.clear();
    }
}