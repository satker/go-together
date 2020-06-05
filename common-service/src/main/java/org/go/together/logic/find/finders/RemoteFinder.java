package org.go.together.logic.find.finders;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.exceptions.IncorrectFindObject;
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
            Map<String, FieldMapper> fieldMappers = FieldParser.getFieldMapperByRemoteField(availableFields, key);
            fieldMappers.forEach((localField, fieldMapper) ->
                    convertToAnotherRequest(filtersToAnotherServices, key,
                            FieldParser.getFieldSearch(key),
                            fieldMapper, value));
        });
        if (!filtersToAnotherServices.isEmpty()) {
            return getFilterResultFromOtherServices(filtersToAnotherServices);
        }

        return Collections.emptyMap();
    }

    private void convertToAnotherRequest(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices,
                                         String currentLocalField,
                                         String anotherServiceSearchField,
                                         FieldMapper fieldMapper,
                                         FilterDto value) {
        ClientLocalFieldObject clientLocalFieldObject = ClientLocalFieldObject.builder()
                .client(fieldMapper.getRemoteServiceClient())
                .localField(FieldParser.getParsedString(currentLocalField)[0]).build();
        FormDto stringFilterDtoMap = filtersToAnotherServices.get(clientLocalFieldObject);
        FormDto remoteClientFormDto = getRemoteClientFormDto(anotherServiceSearchField, value, stringFilterDtoMap, fieldMapper);
        filtersToAnotherServices.put(clientLocalFieldObject, remoteClientFormDto);
    }

    private FormDto getRemoteClientFormDto(String anotherServiceSearchField,
                                           FilterDto value,
                                           FormDto stringFilterDtoMap,
                                           FieldMapper fieldMapper) {
        Pair<String, String> remoteMainAndGetField = getRemoteMainAndGetField(anotherServiceSearchField, fieldMapper);
        if (stringFilterDtoMap != null) {
            Map<String, FilterDto> filters = stringFilterDtoMap.getFilters();
            FilterDto enrichedFilterDto = getFilterDtoWithValues(remoteMainAndGetField.getRight(), value, filters);
            filters.put(remoteMainAndGetField.getRight(), enrichedFilterDto);
            return new FormDto(null, filters, remoteMainAndGetField.getLeft());
        } else {
            Map<String, FilterDto> map = new HashMap<>();
            map.put(remoteMainAndGetField.getRight(), value);
            return new FormDto(null, map, remoteMainAndGetField.getLeft());
        }
    }

    private Pair<String, String> getRemoteMainAndGetField(String anotherServiceSearchField, FieldMapper fieldMapper) {
        String remoteGetField = fieldMapper.getPathRemoteFieldGetter();

        String[] havingCondition = FieldParser.getSplitHavingCountString(anotherServiceSearchField);
        if (havingCondition.length > 1) {
            try {
                int havingNumber = Integer.parseInt(havingCondition[1]);
                remoteGetField = remoteGetField + ":" + havingNumber;
                return Pair.of(remoteGetField, havingCondition[0]);
            } catch (NumberFormatException e) {
                throw new IncorrectFindObject("Incorrect having condition: " + havingCondition[1]);
            }
        }
        return Pair.of(remoteGetField, anotherServiceSearchField);
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
