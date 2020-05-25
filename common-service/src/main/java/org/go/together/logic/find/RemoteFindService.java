package org.go.together.logic.find;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.exceptions.RemoteClientFindException;
import org.go.together.exceptions.ServiceTransportException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RemoteFindService {
    private final String serviceName;

    public RemoteFindService(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, Collection<Object>> getFiltersToRequest(Map<String, FilterDto> currentFilters,
                                                               Map<String, FilterDto> filters,
                                                               Map<String, FieldMapper> availableFields) {
        Map<String, FormDto> filtersToAnotherServices = new HashMap<>();
        filters.forEach((key, value) -> {
            FieldMapper fieldMapper = FieldParser.getFieldMapper(availableFields, key);
            if (fieldMapper.getRemoteServiceName() == null) {
                currentFilters.put(fieldMapper.getCurrentServiceField(), value);
            } else if (FieldParser.isFieldParsed(key)
                    && StringUtils.isNotBlank(fieldMapper.getRemoteServiceFieldGetter())
                    && fieldMapper.getRemoteServiceName().matches(".*-service")) {
                convertToAnotherRequest(filtersToAnotherServices, key, fieldMapper, value);
            }
        });
        if (!filtersToAnotherServices.isEmpty()) {
            return getFilterResultFromOtherServices(filtersToAnotherServices);
        }

        return Collections.emptyMap();
    }

    private void convertToAnotherRequest(Map<String, FormDto> filtersToAnotherServices,
                                         String key,
                                         FieldMapper fieldMapper,
                                         FilterDto value) {
        FormDto stringFilterDtoMap = filtersToAnotherServices.get(serviceName);

        String serviceLocalField = fieldMapper.getRemoteServiceName() + "&" + FieldParser.getLocalEntityField(key);

        String keyFilterDto = FieldParser.getFieldSearch(key);
        String remoteGetField = fieldMapper.getRemoteServiceMapping() + "." + fieldMapper.getRemoteServiceFieldGetter();

        if (stringFilterDtoMap != null) {
            Map<String, FilterDto> filters = stringFilterDtoMap.getFilters();
            FilterDto filterDto = filters.get(keyFilterDto);
            if (filterDto != null) {
                filterDto.addValue(value.getValues());
            } else {
                filterDto = value;
            }
            filters.put(keyFilterDto, filterDto);
            FormDto formDto1 = new FormDto(null, filters, remoteGetField, false);
            filtersToAnotherServices.put(serviceLocalField, formDto1);
        } else {
            Map<String, FilterDto> map = new HashMap<>();
            map.put(keyFilterDto, value);
            FormDto formDto1 = new FormDto(null, map, remoteGetField, false);
            filtersToAnotherServices.put(serviceLocalField, formDto1);
        }
    }

    protected Map<String, Collection<Object>> getFilterResultFromOtherServices(Map<String, FormDto> filtersToAnotherServices) {
        Map<String, Collection<Object>> result = new HashMap<>();
        for (Map.Entry<String, FormDto> filtersService : filtersToAnotherServices.entrySet()) {
            String[] serviceField = filtersService.getKey().split("&");
            Collection<Object> request = sendRequestAndGetFoundIds(serviceField[0], filtersService.getValue());
            if (request == null || request.isEmpty()) {
                return Collections.emptyMap();
            } else {
                if (result.get(serviceField[1]) == null) {
                    result.put(serviceField[1], request);
                } else {
                    result.get(serviceField[1]).addAll(request);
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
            throw new RemoteClientFindException(serviceId, e);
        }
    }
}
