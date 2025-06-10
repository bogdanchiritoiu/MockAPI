package com.mock.restapi.service.generator;

import com.mock.restapi.model.MockApiDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService
{
    private final MockDataGeneratorService mockDataGeneratorService;

    @Autowired
    public DataService(MockDataGeneratorService mockDataGeneratorService)
    {
        this.mockDataGeneratorService = mockDataGeneratorService;
    }

    public String generateAndProcessData(MockApiDefinition definition)
    {
        String jsonData = mockDataGeneratorService.generatedMockData(definition);
        System.out.println("Generated JSON data: " + jsonData);

        return mockDataGeneratorService.generatedMockData(definition);
    }
}
