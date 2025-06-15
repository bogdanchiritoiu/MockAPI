package com.mock.generator;

import com.mock.model.MockApiDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService
{
    private final DataGeneratorService dataGeneratorService;

    @Autowired
    public DataService(DataGeneratorService dataGeneratorService)
    {
        this.dataGeneratorService = dataGeneratorService;
    }

    public String generateAndProcessData(MockApiDefinition definition)
    {
        String jsonData = dataGeneratorService.generatedMockData(definition);
        System.out.println("Generated JSON data: " + jsonData);

        return dataGeneratorService.generatedMockData(definition);
    }
}
