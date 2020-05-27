package org.go.together.logic.find.finders;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.exceptions.RemoteClientFindException;
import org.go.together.interfaces.FindClient;
import org.go.together.logic.find.utils.ClientLocalFieldObject;
import org.go.together.logic.find.utils.FieldParser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RemoteFinder implements Finder<Collection<Object>> {
    public Map<String, Collection<Object>> getFilters(Map<String, FilterDto> filters,
                                                      Map<String, FieldMapper> availableFields) {
        Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices = new HashMap<>();
        filters.forEach((key, value) -> {
            FieldMapper fieldMapper = FieldParser.getFieldMapper(availableFields, key);
            if (StringUtils.isNotBlank(fieldMapper.getRemoteServiceFieldGetter())
                    && fieldMapper.getRemoteServiceClient() != null) {
                String anotherServiceSearchField = FieldParser.getFieldSearch(key);
                convertToAnotherRequest(filtersToAnotherServices, anotherServiceSearchField, fieldMapper, value);
            }
        });
        if (!filtersToAnotherServices.isEmpty()) {
            return getFilterResultFromOtherServices(filtersToAnotherServices);
        }

        return Collections.emptyMap();
    }

    private void convertToAnotherRequest(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices,
                                         String anotherServiceSearchField,
                                         FieldMapper fieldMapper,
                                         FilterDto value) {
        ClientLocalFieldObject clientLocalFieldObject = ClientLocalFieldObject.builder()
                .client(fieldMapper.getRemoteServiceClient())
                .localField(fieldMapper.getCurrentServiceField()).build();

        FormDto stringFilterDtoMap = filtersToAnotherServices.get(clientLocalFieldObject);

        String remoteGetField = fieldMapper.getRemoteServiceName() + "." + fieldMapper.getRemoteServiceFieldGetter();

        if (stringFilterDtoMap != null) {
            Map<String, FilterDto> filters = stringFilterDtoMap.getFilters();
            FilterDto filterDto = filters.get(anotherServiceSearchField);
            if (filterDto != null) {
                filterDto.addValue(value.getValues());
            } else {
                filterDto = value;
            }
            filters.put(anotherServiceSearchField, filterDto);
            FormDto formDto1 = new FormDto(null, filters, remoteGetField);
            filtersToAnotherServices.put(clientLocalFieldObject, formDto1);
        } else {
            Map<String, FilterDto> map = new HashMap<>();
            map.put(anotherServiceSearchField, value);
            FormDto formDto1 = new FormDto(null, map, remoteGetField);
            filtersToAnotherServices.put(clientLocalFieldObject, formDto1);
        }
    }

    private Map<String, Collection<Object>> getFilterResultFromOtherServices(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices) {
        Map<String, Collection<Object>> result = new HashMap<>();
        for (Map.Entry<ClientLocalFieldObject, FormDto> filtersService : filtersToAnotherServices.entrySet()) {
            Collection<Object> request = sendRequestAndGetFoundIds(filtersService.getKey().getClient(), filtersService.getValue());
            if (request == null || request.isEmpty()) {
                return null;
            } else {
                String localField = filtersService.getKey().getLocalField();
                if (result.get(localField) == null) {
                    result.put(localField, request);
                } else {
                    result.get(localField).addAll(request);
                }
            }
        }
        return result;
    }

    // keys from another service
    private Collection<Object> sendRequestAndGetFoundIds(FindClient client, FormDto body) {
        try {
            ResponseDto<Object> map = client.find(body);
            return map.getResult();
        } catch (Exception e) {
            throw new RemoteClientFindException(e);
        }
    }
}
