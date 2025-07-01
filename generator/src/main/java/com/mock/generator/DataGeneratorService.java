package com.mock.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mock.database.entity.GeneratedData;
import com.mock.database.repository.GeneratedDataRepository;
import com.mock.model.MockApiDefinition;
import org.springframework.stereotype.Service;
import com.mock.generator.util.ValueGenerator;

import java.util.Map;


/**
 * A service responsible for generating mock data based on a given API definition.
 * This service leverages the {@code ObjectMapper} to construct JSON responses,
 * which adhere to the structure and data types specified in the provided {@code com.mock.model.MockApiDefinition}.
 * <p>
 * The generated mock data is returned as a JSON-formatted string, containing
 * an array of objects structured according to the field definitions in the API definition.
 */
@Service
public class DataGeneratorService
{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GeneratedDataRepository generatedDataRepository;

    public DataGeneratorService(GeneratedDataRepository generatedDataRepository)
    {
        this.generatedDataRepository = generatedDataRepository;
    }

    /*
       Generate mock data based on the provided com.mock.model.MockApiDefinition
     */
    public void generatedMockData(MockApiDefinition definition)
    {
        try
        {
            ArrayNode arrayNode = objectMapper.createArrayNode();

            for (int i = 0; i < definition.getCount(); i++)
            {
                arrayNode.add(generateSingleEntry(definition.getName(), definition.getFields()));
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to generate mock data", e);
        }
    }

    private ObjectNode generateSingleEntry(String endpoint, Map<String, Class<?>> fields)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, Class<?>> entry : fields.entrySet())
        {
            String fieldName = entry.getKey();
            Object fieldValue = ValueGenerator.generateValueForType(entry.getValue(), fieldName);

            objectNode.put(fieldName, String.valueOf(fieldValue));
        }
        // TODO extract the nextInternalId generator in a separate method, anb add more checks
        int nextInternalId = generatedDataRepository
                .findTopByEndpointOrderByInternalIdDesc(endpoint)
                .map(GeneratedData::getInternalId)
                .orElse(0) + 1;
        GeneratedData newGeneratedData = new GeneratedData(endpoint, objectNode.toPrettyString());
        newGeneratedData.setInternalId(nextInternalId);

        generatedDataRepository.save(newGeneratedData);

        return objectNode;
    }
}
