package com.mock.generator;

import com.mock.model.MockApiDefinition;
import com.mock.model.MockApiDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataStartupRunner implements ApplicationRunner
{
    private final DataGeneratorService dataGeneratorService;
    private final MockApiDefinitionRegistry registry;

    public DataStartupRunner(DataGeneratorService dataGeneratorService)
    {
        this.dataGeneratorService = dataGeneratorService;
        this.registry             = MockApiDefinitionRegistry.getInstance();
    }

    @Override
    public void run(ApplicationArguments args)
    {
        for (MockApiDefinition def : registry.getDefinitions())
        {
            dataGeneratorService.generatedMockData(def);
        }
    }

}
