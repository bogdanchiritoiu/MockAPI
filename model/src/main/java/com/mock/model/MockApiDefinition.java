package com.mock.model;

import org.springframework.web.bind.annotation.RequestMethod;
import com.mock.model.util.FieldTypeConverter;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Configuration holder for mock API endpoints.
 * Contains endpoint name, field definitions, HTTP methods, and entry count.
 */
public class MockApiDefinition
{
    private final String name;

    /** Name/path of the mock API endpoint */
    private final String endpointName;
    
    /** Map of field names to their Java types */
    private final Map<String, Class<?>> fields;
    
    /** Supported HTTP methods */
    private final Set<RequestMethod> methods;
    
    /** Number of mock entries to generate */
    private final int count;

    /**
     * Creates a new com.mock.model.MockApiDefinition.
     * @throws NullPointerException if any required parameter is null
     */
    public MockApiDefinition(String name, String endpointName, Map<String, String> fields, String methods, int count)
    {
        this.name = name;
        this.endpointName = Objects.requireNonNull(endpointName, "Endpoint name must not be null");
        this.fields = FieldTypeConverter.convertFieldTypes(Objects.requireNonNull(fields, "Fields must not be null"));
        this.methods = FieldTypeConverter.convertToRequestMethods(Objects.requireNonNull(methods, "Methods must not be null"));
        this.count = count;
    }

    public String getName()
    {
        return name;
    }

    public String getEndpointName()
    {
        return endpointName;
    }

    public Map<String, Class<?>> getFields()
    {
        return fields;
    }

    public Set<RequestMethod> getMethods()
    {
        return methods;
    }

    public int getCount()
    {
        return count;
    }

    @Override
    public String toString()
    {
        return "com.mock.model.MockApiDefinition{" +
                "endpointName='" + endpointName + '\'' +
                ", count=" + count +
                ", fields=" + fields +
                ", methods='" + methods + '\'' +
                '}';
    }
}