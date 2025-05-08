package com.mock.restapi.model;

import com.mock.restapi.model.utils.FieldTypeConverter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a mock API endpoint definition containing configuration details for generating mock responses.
 * This class holds information about the endpoint name, field definitions, supported HTTP methods,
 * and the number of mock entries to generate.
 *
 * <p>The class ensures all essential components are non-null through constructor validation.
 */
public class MockApiDefinition
{
    private final String endpointName;
    private final Map<String, Class<?>> fields;
    private final Set<RequestMethod> methods;
    private final int count;

    /**
     * Constructs a new MockApiDefinition with the specified parameters.
     *
     * @param endpointName the name/path of the endpoint (must not be null)
     * @param fields       a map of field names to their type representations (must not be null)
     * @param methods      a string representing the supported HTTP methods (must not be null)
     * @param count        the number of mock entries to generate
     * @throws NullPointerException if endpointName, fields, or methods is null
     */
    public MockApiDefinition(String endpointName, Map<String, String> fields, String methods, int count)
    {
        this.endpointName = Objects.requireNonNull(endpointName, "Endpoint name must not be null");
        this.fields = FieldTypeConverter.convertFieldTypes(Objects.requireNonNull(fields, "Fields must not be null"));
        this.methods = FieldTypeConverter.convertToRequestMethods(Objects.requireNonNull(methods, "Methods must not be null"));
        this.count = count;
    }

    /**
     * Returns the name/path of the mock API endpoint.
     *
     * @return the endpoint name
     */
    public String getEndpointName() {
        return endpointName;
    }

    /**
     * Returns the map of field names to their corresponding Java types.
     *
     * @return the fields map
     */
    public Map<String, Class<?>> getFields() {
        return fields;
    }

    /**
     * Returns the set of supported HTTP request methods for this endpoint.
     *
     * @return the set of request methods
     */
    public Set<RequestMethod> getMethods() {
        return methods;
    }

    /**
     * Returns the number of mock entries to generate.
     *
     * @return the count of entries
     */
    public int getCount()
    {
        return count;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MockApiDefinition that = (MockApiDefinition) o;
//        return count == that.count &&
//                Objects.equals(endpointName, that.endpointName) &&
//                Objects.equals(fields, that.fields) &&
//                Objects.equals(methods, that.methods);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(endpointName, count, fields, methods);
//    }

    @Override
    public String toString() {
        return "MockApiDefinition{" +
                "endpointName='" + endpointName + '\'' +
                ", count=" + count +
                ", fields=" + fields +
                ", methods='" + methods + '\'' +
                '}';
    }
}