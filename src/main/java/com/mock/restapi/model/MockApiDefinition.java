package com.mock.restapi.model;

import com.mock.restapi.model.utils.FieldTypeConverter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

//TODO add java doc and comments
public class MockApiDefinition
{
    private final String endpointName;
    private final Map<String, Class<?>> fields;
    private final Set<RequestMethod> methods;
    private final int count;

    public MockApiDefinition(String endpointName, Map<String, String> fields, String methods, int count)
    {
        this.endpointName = Objects.requireNonNull(endpointName, "Endpoint name must not be null");
        this.fields = FieldTypeConverter.convertFieldTypes(Objects.requireNonNull(fields, "Fields must not be null"));
        this.methods = FieldTypeConverter.convertToRequestMethods(Objects.requireNonNull(methods, "Methods must not be null"));
        this.count = count;
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