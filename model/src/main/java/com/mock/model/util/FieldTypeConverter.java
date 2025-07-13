package com.mock.model.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class that provides type conversion functionality for field types and HTTP request methods.
 * This class serves two main purposes:
 * <p>
 * 1. Converting string representations of Java types to their corresponding Class objects
 * - Supports primitive wrapper types (Integer, Long, etc.)
 * - Supports String and common data types
 * - Provides validation for supported types
 * <p>
 * 2. Converting string representations of HTTP methods to RequestMethod enums
 * - Supports individual HTTP methods (GET, POST, etc.)
 * - Supports "all" keyword for all main HTTP methods
 * - Handles comma-separated lists of HTTP methods
 * <p>
 * All methods in this class are static, and the class cannot be instantiated.
 *
 */
public final class FieldTypeConverter
{
    private static final Map<String, Class<?>> TYPE_MAP = new HashMap<>();
    private static final Logger LOG = LogManager.getLogger();
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
        LOG.trace("Converting type name {} to {}", typeName, TYPE_MAP.get(typeName.toLowerCase()).toGenericString());
        return TYPE_MAP.get(typeName.toLowerCase());
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
        LOG.trace("Converted field types size: {}", classTypeMap.size());
        LOG.trace("Converted field types: {}", classTypeMap.values());
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
            return getAllRequestMethods();
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
        
        LOG.trace("Input methods string: '{}', Converted HTTP methods: {}", methodsString, methods);
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