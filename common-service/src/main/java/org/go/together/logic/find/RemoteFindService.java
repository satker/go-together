package org.go.together.logic.find;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.exceptions.ServiceTransportException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class RemoteFindService {
    private final String serviceName;

    public RemoteFindService(String serviceName) {
        this.serviceName = serviceName;
    }

    public Collection<Object> getFiltersToRequest(Map<String, FilterDto> currentFilters,
                                                  FormDto dto) {
        Map<String, FormDto> filtersToAnotherServices = new HashMap<>();
        dto.getFilters().forEach((key, value) -> {
            String[] keys = key.split("\\.", 2);
            if (keys[0].equals(serviceName)) {
                currentFilters.put(keys[1], value);
            } else if (keys[0].matches(".*-service.*")) {
                convertToAnotherRequest(dto, filtersToAnotherServices, key, value);
            }
        });
        if (!filtersToAnotherServices.isEmpty()) {
            return getFilterResultFromOtherServices(filtersToAnotherServices);
        }

        return Collections.emptyList();
    }

    private void convertToAnotherRequest(FormDto formDto, Map<String, FormDto> filtersToAnotherServices, String key, FilterDto value) {
        String[] serviceAndValue = key.split("\\.", 3);
        if (!serviceAndValue[0].matches(".*-service")) {
            throw new IncorrectDtoException("Incorrect service name");
        }

        FormDto stringFilterDtoMap = filtersToAnotherServices.get(serviceAndValue[0]);
        String keyFilterDto = serviceAndValue[1] + "." + serviceAndValue[2];
        String mainField = serviceAndValue[1] + "." + formDto.getMainIdField();
        if (stringFilterDtoMap != null) {
            Map<String, FilterDto> filters = stringFilterDtoMap.getFilters();
            FilterDto filterDto = filters.get(keyFilterDto);
            if (filterDto != null) {
                filterDto.addValue(value.getValues());
            } else {
                filterDto = value;
            }
            filters.put(keyFilterDto, filterDto);
            FormDto formDto1 = new FormDto(null, filters, mainField, false);
            filtersToAnotherServices.put(serviceAndValue[0], formDto1);
        } else {
            Map<String, FilterDto> map = new HashMap<>();
            map.put(keyFilterDto, value);
            FormDto formDto1 = new FormDto(null, map, mainField, false);
            filtersToAnotherServices.put(serviceAndValue[0], formDto1);
        }
    }

    protected Collection<Object> getFilterResultFromOtherServices(Map<String, FormDto> filtersToAnotherServices) {
        Collection<Object> result = new HashSet<>();
        for (Map.Entry<String, FormDto> stringFormDtoEntry : filtersToAnotherServices.entrySet()) {
            Collection<Object> request = sendRequestAndGetFoundIds(stringFormDtoEntry.getKey(), stringFormDtoEntry.getValue());
            if (request == null || request.isEmpty()) {
                return Collections.emptySet();
            } else {
                if (result.isEmpty()) {
                    result.addAll(request);
                } else {
                    result.retainAll(request);
                }
            }
        }
        return result;
    }

    // keys from another service
    private Collection<Object> sendRequestAndGetFoundIds(String serviceId, FormDto body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writeValueAsString(body);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/services/" + serviceId + "/find"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ServiceTransportException("Service " + serviceId + " is unavailable.");
            }

            ResponseDto map = objectMapper.readValue(response.body(), ResponseDto.class);
            return map.getResult();
        } catch (Exception e) {

        }
        return null;
    }
}
