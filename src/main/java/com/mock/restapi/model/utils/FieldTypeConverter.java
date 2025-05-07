package com.mock.restapi.model.utils;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//TODO add java doc and comments
//TODO maybe split the file HTTP request and field converter OR use proper class naem
public final class FieldTypeConverter
{
    private static final Map<String, Class<?>> TYPE_MAP = new HashMap<>();

    static
    {
        // Initialize primitive and common types
        TYPE_MAP.put("string", String.class);
        TYPE_MAP.put("word", String.class);
        TYPE_MAP.put("integer", Integer.class);
        TYPE_MAP.put("number", Integer.class);
        TYPE_MAP.put("int", Integer.class);
        TYPE_MAP.put("long", Long.class);
        TYPE_MAP.put("double", Double.class);
        TYPE_MAP.put("float", Float.class);
        TYPE_MAP.put("boolean", Boolean.class);
        TYPE_MAP.put("character", Character.class);
        TYPE_MAP.put("byte", Byte.class);
        TYPE_MAP.put("short", Short.class);
    }

    private FieldTypeConverter()
    {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a string representation of a type to its corresponding Class object.
     *
     * @param typeName the string representation of the type (e.g., "String", "Integer")
     * @return the corresponding Class object, or null if the type is not supported
     */
    public static Class<?> convertToClass(String typeName)
    {
        return TYPE_MAP.get(typeName.toLowerCase());
    }

    /**
     * Checks if the given type name is supported.
     *
     * @param typeName the string representation of the type
     * @return true if the type is supported, false otherwise
     */
    public static boolean isSupported(String typeName)
    {
        return TYPE_MAP.containsKey(typeName.toLowerCase());
    }

    /**
     * Converts a map of field names and type strings to a map of field names and Class objects.
     *
     * @param stringTypeMap Map with field names and their type names as strings
     * @return Map with field names and their corresponding Class objects
     * @throws IllegalArgumentException if any type is not supported
     */
    public static Map<String, Class<?>> convertFieldTypes(Map<String, String> stringTypeMap)
    {
        Map<String, Class<?>> classTypeMap = new HashMap<>();
        
        for (Map.Entry<String, String> entry : stringTypeMap.entrySet())
        {
            Class<?> type = convertToClass(entry.getValue());
            if (type == null)
            {
                throw new IllegalArgumentException("Unsupported type: " + entry.getValue());
            }
            classTypeMap.put(entry.getKey(), type);
        }
        
        return classTypeMap;
    }
    
    /**
     * Converts a string representation of HTTP methods to a Set of RequestMethod enums.
     * Handles "all" keyword and comma-separated lists of HTTP methods.
     *
     * @param methodsString the string representation (e.g., "GET, POST" or "all")
     * @return Set of RequestMethod enums representing the specified methods
     * @throws IllegalArgumentException if the input contains invalid HTTP methods
     */
    public static Set<RequestMethod> convertToRequestMethods(String methodsString)
    {
        if (methodsString == null || methodsString.trim().isEmpty())
        {
            throw new IllegalArgumentException("HTTP methods string cannot be null or empty");
        }
        
        // Handle the "all" case
        if (methodsString.trim().equalsIgnoreCase("all"))
        {
            return EnumSet.of(RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE);
        }
        
        // Handle comma-separated list of methods
        Set<RequestMethod> methods = Arrays.stream(methodsString.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(method -> {
                    try
                    {
                        return RequestMethod.valueOf(method);
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new IllegalArgumentException("Unsupported HTTP method: " + method);
                    }
                })
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(RequestMethod.class)));
        
        if (methods.isEmpty())
        {
            throw new IllegalArgumentException("No valid HTTP methods provided in: " + methodsString);
        }
        
        return methods;
    }
    
    /**
     * Returns all supported HTTP methods from Spring's RequestMethod enum.
     *
     * @return Set of all RequestMethod values
     */
    public static Set<RequestMethod> getAllRequestMethods()
    {
        return EnumSet.allOf(RequestMethod.class);
    }
}