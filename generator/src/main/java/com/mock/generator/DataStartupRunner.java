package com.mock.generator;

import com.mock.model.MockApiDefinition;
import com.mock.model.MockApiDefinitionRegistry;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * A class that executes data generation tasks at application startup.
 * It iterates through registered mock API definitions and generates mock data using the {@link DataGeneratorService}.
 * <p>
 * This class implements the {@link ApplicationRunner} interface, ensuring execution after the application context has been initialized.
 * The {@link MockApiDefinitionRegistry} is used to retrieve all mock API definitions for processing.
 */
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
