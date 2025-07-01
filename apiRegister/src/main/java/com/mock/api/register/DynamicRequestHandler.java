package com.mock.api.register;

import com.mock.api.constants.ApiConstants;
import com.mock.database.entity.GeneratedData;
import com.mock.database.repository.GeneratedDataRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DynamicRequestHandler
{
    private final GeneratedDataRepository generatedDataRepository;

    public DynamicRequestHandler(GeneratedDataRepository generatedDataRepository)
    {
        this.generatedDataRepository = generatedDataRepository;
    }

    @ResponseBody
    public ResponseEntity<String> handleGet(HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        String endpoint = uri.substring(ApiConstants.API_BASE_PATH.length());

        String allData = generatedDataRepository.findByEndpoint(endpoint)
                .stream()
                .map(GeneratedData::getData)
                .collect(Collectors.joining("\n---\n")); // Joins each data string with a separator

        String response = "Resources:\n" + allData + "\n";
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    public ResponseEntity<String> handleGet(@PathVariable String id, HttpServletRequest request)
    {
        // TODO better extraction for endpoint
        // TODO handle when id is invalid or > 'count'
        String uri = request.getRequestURI();
        String endpoint = uri.substring(ApiConstants.API_BASE_PATH.length()).split("/")[0];

        Optional<GeneratedData> dataOpt =
                generatedDataRepository
                        .findByEndpointAndInternalId(endpoint, Integer.parseInt(id));

        if (dataOpt.isEmpty())
        {
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
    public ResponseEntity<String> handlePost(@RequestBody Map<String, Object> ignoredRequestBody)
    {
        // TODO
        return ResponseEntity.status(HttpStatus.CREATED).body("Resource created successfully");
    }

    @ResponseBody
    public ResponseEntity<String> handlePut(
            @PathVariable String id,
            @RequestBody Map<String, Object> ignoredRequestBody)
    {
        // TODO
        return ResponseEntity.ok("Resource " + id + " updated successfully");
    }

    @ResponseBody
    public ResponseEntity<String> handleDelete(@PathVariable String id)
    {
        // TODO
        return ResponseEntity.ok("Resource " + id + " deleted successfully");
    }
}
