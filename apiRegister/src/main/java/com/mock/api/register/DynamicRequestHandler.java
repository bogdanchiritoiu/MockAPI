package com.mock.api.register;

import com.mock.api.constants.ApiConstants;
import com.mock.database.entity.GeneratedData;
import com.mock.database.repository.GeneratedDataRepository;
import com.mock.model.MockApiDefinitionRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
* Class that is responsible for handling all the request received
* Get, PUT, POST and DELETE
* */
@Component
public class DynamicRequestHandler
{
    private final GeneratedDataRepository generatedDataRepository;
    private final Logger LOG = LogManager.getLogger();

    public DynamicRequestHandler(GeneratedDataRepository generatedDataRepository)
    {
        this.generatedDataRepository = generatedDataRepository;
    }

    /* Get all generated data for a specific endpoint (based on the requestURI */
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<String> handleGet(HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        String endpoint = uri.substring(ApiConstants.API_BASE_PATH.length());
        LOG.info("Received GET request with path: {}", uri);


        String allData = generatedDataRepository.findByEndpoint(endpoint)
                .stream()
                .map(GeneratedData::getData)
                .collect(Collectors.joining("\n---\n")); // Joins each data string with a separator
        LOG.trace("All data for endpoint: {}:\n{}", endpoint, allData);

        String response = "Resources:\n" + allData + "\n";
        return ResponseEntity.ok(response);
    }

    /* Get the generated data for a specific endpoint (get from requestURI) based on the ID variable */
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<String> handleGet(@PathVariable String id, HttpServletRequest request)
    {
        // TODO better extraction for endpoint
        String uri = request.getRequestURI();
        String endpoint = uri.substring(ApiConstants.API_BASE_PATH.length()).split("/")[0];

        Optional<GeneratedData> dataOpt =
                generatedDataRepository
                        .findByEndpointAndInternalId(endpoint, Integer.parseInt(id));

        int count = MockApiDefinitionRegistry.getInstance().getDefinitions().stream().filter(def -> def.getEndpointName().equals(endpoint)).findFirst().get().getCount();

        if (dataOpt.isEmpty())
        {
            if (Integer.parseInt(id) > count)
            {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("{\"error\":\"The ID " + id + " is over the maximum number of entries (maxCount: "+ count +") for endpoint: " + endpoint + "\"}");
            }

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Resource not found\"}");
        }

        String response = dataOpt.get().getData();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);

    }

    @ResponseBody
    @Transactional
    public ResponseEntity<String> handlePost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        String endpoint = uri.substring(ApiConstants.API_BASE_PATH.length()).split("/")[0];

        
        // TODO check if requestBody is a valid json with all field existing
        int nextInternalId = generatedDataRepository
                .findTopByEndpointOrderByInternalIdDesc(endpoint)
                .map(GeneratedData::getInternalId)
                .orElse(0) + 1;

        GeneratedData newGeneratedData = new GeneratedData(endpoint, requestBody.toString());
        newGeneratedData.setInternalId(nextInternalId);
        generatedDataRepository.save(newGeneratedData);

        return ResponseEntity.status(HttpStatus.CREATED).body("Resource created successfully");
    }

    /*
    * Method that update or create data
    * */
    @ResponseBody
    @Transactional
    public ResponseEntity<String> handlePut(
            @PathVariable String id,
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {
        // Get the endpoint
        String uri = request.getRequestURI();
        String endpoint = uri.substring(ApiConstants.API_BASE_PATH.length()).split("/")[0];

        // Check if the generatedData exist
        Optional<GeneratedData> generatedData = generatedDataRepository.findByEndpointAndInternalId(endpoint, Integer.parseInt(id));

        // ToDo - Check if the requestBody(json) expect the same format. Either to have all fields or replace only the fields that are valid
        // If there is no data for the given id - insert a new one based on the requestBody json
        if (generatedData.isEmpty())
        {
            GeneratedData newGeneratedData = new GeneratedData(endpoint, requestBody.toString());
            newGeneratedData.setInternalId(Integer.parseInt(id));
            generatedDataRepository.save(newGeneratedData);
            return ResponseEntity.ok("A new entry was added with id:  " + id + " for endpoint: " + endpoint);
        }
        else
        {
            // If there is data, update (replace the content)
            GeneratedData dataToUpdate = generatedData.get();
            dataToUpdate.setData(requestBody.toString());

            generatedDataRepository.save(dataToUpdate);
            return ResponseEntity.ok("Resource " + id + " updated successfully");
        }
    }

    @ResponseBody
    @Transactional
    public ResponseEntity<String> handleDelete(@PathVariable String id, HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        String endpoint = uri.substring(ApiConstants.API_BASE_PATH.length()).split("/")[0];

        long result = generatedDataRepository.deleteByEndpointAndInternalId(endpoint, Integer.parseInt(id));

        if (result != 1)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Resource not found\"}");
        }

        return ResponseEntity.ok("Resource " + id + " deleted successfully");
    }
}
