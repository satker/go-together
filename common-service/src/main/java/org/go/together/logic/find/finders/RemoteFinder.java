package org.go.together.logic.find.finders;

import org.apache.commons.lang3.StringUtils;
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
            Map<String, FieldMapper> fieldMappers = FieldParser.getFieldMappers(availableFields, key);
            fieldMappers.forEach((field, fieldMapper) -> {
                String localFieldForSearch = FieldParser.getLocalFieldForSearch(fieldMappers, field);
                getFiltersToAnotherService(filtersToAnotherServices, localFieldForSearch, value, fieldMapper,
                        FieldParser.getFieldSearch(key));
            });
        });
        if (!filtersToAnotherServices.isEmpty()) {
            return getFilterResultFromOtherServices(filtersToAnotherServices);
        }

        return Collections.emptyMap();
    }

    private void getFiltersToAnotherService(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices,
                                            String currentServiceSearchField,
                                            FilterDto filterDto,
                                            FieldMapper fieldMapper,
                                            String anotherServiceSearchField) {
        if (StringUtils.isNotBlank(fieldMapper.getPathRemoteFieldGetter())
                && fieldMapper.getRemoteServiceClient() != null) {
            convertToAnotherRequest(currentServiceSearchField,
                    filtersToAnotherServices,
                    anotherServiceSearchField,
                    fieldMapper,
                    filterDto);
        }
    }

    private void convertToAnotherRequest(String key,
                                         Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices,
                                         String anotherServiceSearchField,
                                         FieldMapper fieldMapper,
                                         FilterDto value) {
        ClientLocalFieldObject clientLocalFieldObject = ClientLocalFieldObject.builder()
                .client(fieldMapper.getRemoteServiceClient())
                .localField(key).build();
        FormDto stringFilterDtoMap = filtersToAnotherServices.get(clientLocalFieldObject);
        FormDto remoteClientFormDto = getRemoteClientFormDto(anotherServiceSearchField, value, stringFilterDtoMap, fieldMapper);
        filtersToAnotherServices.put(clientLocalFieldObject, remoteClientFormDto);
    }

    private FormDto getRemoteClientFormDto(String anotherServiceSearchField,
                                           FilterDto value,
                                           FormDto stringFilterDtoMap,
                                           FieldMapper fieldMapper) {
        String remoteGetField = fieldMapper.getPathRemoteFieldGetter();

        if (stringFilterDtoMap != null) {
            Map<String, FilterDto> filters = stringFilterDtoMap.getFilters();
            FilterDto enrichedFilterDto = getFilterDtoWithValues(anotherServiceSearchField, value, filters);
            filters.put(anotherServiceSearchField, enrichedFilterDto);
            return new FormDto(null, filters, remoteGetField);
        } else {
            Map<String, FilterDto> map = new HashMap<>();
            map.put(anotherServiceSearchField, value);
            return new FormDto(null, map, remoteGetField);
        }
    }

    private FilterDto getFilterDtoWithValues(String anotherServiceSearchField, FilterDto value, Map<String, FilterDto> filters) {
        FilterDto filterDto = filters.get(anotherServiceSearchField);
        if (filterDto != null) {
            filterDto.addValue(value.getValues());
        } else {
            filterDto = value;
        }
        return filterDto;
    }

    private Map<String, Collection<Object>> getFilterResultFromOtherServices(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices) {
        Map<String, Collection<Object>> result = new HashMap<>();
        for (Map.Entry<ClientLocalFieldObject, FormDto> filtersService : filtersToAnotherServices.entrySet()) {
            Collection<Object> request = getRemoteResult(filtersService.getKey().getClient(), filtersService.getValue());
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
    private Collection<Object> getRemoteResult(FindClient client, FormDto formDto) {
        try {
            return client.find(formDto).getResult();
        } catch (Exception e) {
            throw new RemoteClientFindException(e);
        }
    }
}
