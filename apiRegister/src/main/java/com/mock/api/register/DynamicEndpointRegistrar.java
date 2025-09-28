package com.mock.api.register;

import com.mock.api.constants.ApiConstants;
import com.mock.model.MockApiDefinition;
import com.mock.model.MockApiDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The com.mock.api.register.DynamicEndpointRegistrar class is responsible for dynamically registering REST API
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
 * Note: com.mock.api.register.DynamicEndpointRegistrar is marked as a Spring {@link Component} for
 * automatic detection and registration as a Spring-managed bean.
 */
@Component
public class DynamicEndpointRegistrar implements SmartInitializingSingleton
{
    private final MockApiDefinitionRegistry definitionRegistry;
    private final RequestMappingHandlerMapping handlerMapping;
    private final DynamicRequestHandler handler;
    private final Logger LOG = LogManager.getLogger();

    @Autowired
    public DynamicEndpointRegistrar(MockApiDefinitionRegistry definitionRegistry,
                                    @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping requestMappingHandlerMapping,
                                    DynamicRequestHandler handler)
    {
        this.definitionRegistry = definitionRegistry;
        this.handlerMapping = requestMappingHandlerMapping;
        this.handler = handler;
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
                LOG.info("Registered dynamic endpoint: {}", definition.getEndpointName());
            }
        }
        catch (Exception e)
        {
            LOG.error("Failed to register dynamic endpoints: {}", e.getMessage());
            throw new RuntimeException("Failed to register endpoints", e);
        }
    }

    private void registerEndpointHandlers(MockApiDefinition definition) throws NoSuchMethodException
    {
        String basePath = ApiConstants.API_BASE_PATH + definition.getEndpointName();

        // Register GET endpoint
        if (definition.getMethods().contains(RequestMethod.GET))
        {
            Method getMethod = DynamicRequestHandler.class.getDeclaredMethod("handleGet", String.class, HttpServletRequest.class);
            Method getAllMethod = DynamicRequestHandler.class.getDeclaredMethod("handleGet", HttpServletRequest.class);

            RequestMappingInfo getMapping = RequestMappingInfo
                    .paths(basePath + ApiConstants.ID_PATH_VARIABLE)
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
            Method postMethod = DynamicRequestHandler.class.getDeclaredMethod("handlePost", Map.class, HttpServletRequest.class);
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
            Method putMethod = DynamicRequestHandler.class.getDeclaredMethod("handlePut", String.class, Map.class, HttpServletRequest.class);
            RequestMappingInfo putMapping = RequestMappingInfo
                    .paths(basePath + ApiConstants.ID_PATH_VARIABLE)
                    .methods(RequestMethod.PUT)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build();
            handlerMapping.registerMapping(putMapping, handler, putMethod);
        }

        // Register DELETE endpoint
        if (definition.getMethods().contains(RequestMethod.DELETE))
        {
            Method deleteMethod = DynamicRequestHandler.class.getDeclaredMethod("handleDelete", String.class, HttpServletRequest.class);
            RequestMappingInfo deleteMapping = RequestMappingInfo
                    .paths(basePath + ApiConstants.ID_PATH_VARIABLE)
                    .methods(RequestMethod.DELETE)
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .build();
            handlerMapping.registerMapping(deleteMapping, handler, deleteMethod);
        }
    }
}