package com.mock.restapi.controller;

import com.mock.restapi.model.MockApiDefinition;
import com.mock.restapi.model.MockApiDefinitionRegistry;
import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * The DynamicEndpointRegistrar class is responsible for dynamically registering REST API
 * endpoints at runtime based on a set of predefined configurations. It retrieves
 * API definitions from a {@link MockApiDefinitionRegistry} and uses Spring's
 * {@link RequestMappingHandlerMapping} for registering handlers for each HTTP method
 * (GET, POST, PUT, DELETE).
 * <p>
 * This class implements {@link ApplicationContextAware} to gain access to the Spring
 * {@link ApplicationContext} and performs endpoint registration during the application
 * startup using the {@link PostConstruct} annotation.
 * <p>
 * It defines a nested static {@link DynamicRequestHandler} class, which acts as a
 * generic handler for processing requests of the dynamically registered endpoints.
 * <p>
 * Note: DynamicEndpointRegistrar is marked as a Spring {@link Component} for
 * automatic detection and registration as a Spring-managed bean.
 */
@Component
//@DependsOn({"mockApiConfigurationService"})
public class DynamicEndpointRegistrar implements ApplicationContextAware, SmartInitializingSingleton
{
    private static final String API_BASE_PATH = "/api/";
    private static final String ID_PATH_VARIABLE = "/{id}";

    private ApplicationContext applicationContext;
    private final MockApiDefinitionRegistry definitionRegistry;
    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public DynamicEndpointRegistrar(MockApiDefinitionRegistry definitionRegistry,
                                    @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping requestMappingHandlerMapping)
    {
        this.definitionRegistry = definitionRegistry;
        this.handlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = Objects.requireNonNull(applicationContext, "ApplicationContext must not be null");
    }

    @Override
    public void afterSingletonsInstantiated()
    {
        registerDynamicEndpoints();
    }

    public void registerDynamicEndpoints()
    {
        try
        {
            for (MockApiDefinition definition : definitionRegistry.getDefinitions())
            {
                registerEndpointHandlers(definition);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to register dynamic endpoints: " + e.getMessage());
            throw new RuntimeException("Failed to register endpoints", e);
        }
    }

    private void registerEndpointHandlers(MockApiDefinition definition) throws NoSuchMethodException {
        String basePath = API_BASE_PATH + definition.getEndpointName();
        DynamicRequestHandler handler = new DynamicRequestHandler();

        // Register GET endpoint
        if (definition.getMethods().contains(RequestMethod.GET))
        {
            Method getMethod = DynamicRequestHandler.class.getDeclaredMethod("handleGet", String.class);
            Method getAllMethod = DynamicRequestHandler.class.getDeclaredMethod("handleGet");

            RequestMappingInfo getMapping = RequestMappingInfo
                    .paths(basePath + "/{id}")
                    .methods(RequestMethod.GET)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build();
            RequestMappingInfo getAllMapping = RequestMappingInfo
                    .paths(basePath)
                    .methods(RequestMethod.GET)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build();

            handlerMapping.registerMapping(getMapping, handler, getMethod);
            handlerMapping.registerMapping(getAllMapping, handler, getAllMethod);
        }

        // Register POST endpoint
        if (definition.getMethods().contains(RequestMethod.POST))
        {
            Method postMethod = DynamicRequestHandler.class.getDeclaredMethod("handlePost", Map.class);
            RequestMappingInfo postMapping = RequestMappingInfo
                    .paths(basePath)
                    .methods(RequestMethod.POST)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build();
            handlerMapping.registerMapping(postMapping, handler, postMethod);
        }

        // Register PUT endpoint
        if (definition.getMethods().contains(RequestMethod.PUT))
        {
            Method putMethod = DynamicRequestHandler.class.getDeclaredMethod("handlePut", String.class, Map.class);
            RequestMappingInfo putMapping = RequestMappingInfo
                    .paths(basePath + "/{id}")
                    .methods(RequestMethod.PUT)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build();
            handlerMapping.registerMapping(putMapping, handler, putMethod);
        }

        // Register DELETE endpoint
        if (definition.getMethods().contains(RequestMethod.DELETE))
        {
            Method deleteMethod = DynamicRequestHandler.class.getDeclaredMethod("handleDelete", String.class);
            RequestMappingInfo deleteMapping = RequestMappingInfo
                    .paths(basePath + "/{id}")
                    .methods(RequestMethod.DELETE)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build();
            handlerMapping.registerMapping(deleteMapping, handler, deleteMethod);
        }
    }

    @Component
    public static class DynamicRequestHandler
    {
        @ResponseBody
        public ResponseEntity<String> handleGet()
        {
            return ResponseEntity.ok("GET all resources");
        }

        @ResponseBody
        public ResponseEntity<String> handleGet(@PathVariable String id)
        {
            return ResponseEntity.ok("GET request processed for ID: " + id);
        }

        @ResponseBody
        public ResponseEntity<String> handlePost(@RequestBody Map<String, Object> requestBody)
        {
            return ResponseEntity.status(HttpStatus.CREATED).body("Resource created successfully");
        }

        @ResponseBody
        public ResponseEntity<String> handlePut(
                @PathVariable String id,
                @RequestBody Map<String, Object> requestBody)
        {
            return ResponseEntity.ok("Resource " + id + " updated successfully");
        }

        @ResponseBody
        public ResponseEntity<String> handleDelete(@PathVariable String id)
        {
            return ResponseEntity.ok("Resource " + id + " deleted successfully");
        }
    }
}