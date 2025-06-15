package com.mock.model;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A registry class responsible for managing and maintaining a collection of {@link MockApiDefinition} objects.
 * This class operates as a singleton and provides functionalities for adding, retrieving, and clearing mock API definitions.
 * <p>
 * The service is initialized as a Spring-managed bean and stores the registered mock API definitions in a thread-safe list.
 */
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