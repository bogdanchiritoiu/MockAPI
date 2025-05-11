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
 *
 * <p>Fields:
 * <ul>
 *     <li>endpointName - The name of the mock API endpoint</li>
 *     <li>fields - Map of field names to their Java class types</li>
 *     <li>methods - Set of supported HTTP request methods</li>
 *     <li>count - Number of mock data entries to generate</li>
 * </ul>
 */
public class MockApiDefinition
{
    /**
     * Represents the name or path of a mock API endpoint.
     * This value is used to uniquely identify the endpoint
     * within the {@code MockApiDefinition} class and facilitate
     * operations such as registration and lookup in the {@code MockApiDefinitionRegistry}.
     * <p>
     * This field is immutable and must not be {@code null}.
     */
    private final String endpointName;
    /**
     * A map representing the fields of the mock API endpoint and their corresponding Java types.
     * The keys in the map are field names, and the values are Class objects that represent the types of those fields.
     * <p>
     * This map is used to define the structure and data types of the mock data fields for the API endpoint.
     * <p>
     * For example, field names could represent entity attributes, and the mapped Class objects
     * specify the Java type that those fields should hold during the mock API generation process.
     * <p>
     * This field is immutable and must be provided during the construction of the {@code MockApiDefinition}.
     */
    private final Map<String, Class<?>> fields;
    /**
     * Represents the set of supported HTTP request methods for a mock API endpoint.
     * <p>
     * This field is initialized during the construction of the {@code MockApiDefinition}
     * object and remains immutable. The methods define the HTTP actions allowed
     * for the associated endpoint, such as GET, POST, PUT, or DELETE.
     */
    private final Set<RequestMethod> methods;
    /**
     * Represents the number of mock entries to generate for the mock API definition.
     * This value is specified during the construction of a {@code MockApiDefinition} instance.
     * It remains immutable once set.
     */
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