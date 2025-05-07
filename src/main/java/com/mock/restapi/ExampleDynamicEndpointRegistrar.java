package com.mock.restapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExampleDynamicEndpointRegistrar implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private List<Map<String, Object>> endpointDefinitions;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void registerDynamicEndpoints() throws IOException, NoSuchMethodException {
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");

        ObjectMapper objectMapper = new ObjectMapper();
        Resource resource = applicationContext.getResource("classpath:endpoints.json");
        InputStream inputStream = resource.getInputStream();
        endpointDefinitions = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});

        Object handler = new DynamicHandler(); // Instance of the class containing handler methods
        Method handleDynamicRequest = DynamicHandler.class.getDeclaredMethod("handle", Map.class, Map.class);

        for (Map<String, Object> endpoint : endpointDefinitions) {
            String path = (String) endpoint.get("path");
            String method = (String) endpoint.get("method");

            RequestMappingInfo.Builder builder = RequestMappingInfo
                    .paths(path)
                    .methods(RequestMethod.valueOf(method.toUpperCase()))
                    .produces(MediaType.APPLICATION_JSON_VALUE); // Assume JSON response

            RequestMappingInfo requestMappingInfo = builder.build();

            requestMappingHandlerMapping.registerMapping(requestMappingInfo, handler, handleDynamicRequest);
        }
    }

    // A simple handler class to process dynamic requests
    @Component
    public static class DynamicHandler {
        @ResponseBody
        public ResponseEntity<?> handle(@PathVariable Map<String, String> pathVariables,
                                        @RequestBody(required = false) Map<String, Object> requestBody,
                                        Map<String, Object> endpointDefinition) {
            Object response = endpointDefinition.get("response");
            Integer responseCode = (Integer) endpointDefinition.getOrDefault("responseCode", 200);

            if (response instanceof Map) {
                // Simple template replacement for path variables in the response
                String jsonResponse = new ObjectMapper().writeValueAsString(response);
                Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
                Matcher matcher = pattern.matcher(jsonResponse);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    String variableName = matcher.group(1);
                    String variableValue = pathVariables.get(variableName);
                    matcher.appendReplacement(sb, variableValue == null ? "" : variableValue);
                }
                matcher.appendTail(sb);
                try {
                    return ResponseEntity.status(HttpStatus.valueOf(responseCode))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new ObjectMapper().readValue(sb.toString(), Map.class));
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing response");
                }
            }

            return ResponseEntity.status(HttpStatus.valueOf(responseCode)).body(response);
        }
    }
}
