package org.go.together.find.finders;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.exceptions.RemoteClientFindException;
import org.go.together.find.client.FindClient;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.utils.FindUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class RemoteFinder implements Finder {
    public Map<FieldDto, Collection<Object>> getFilters(Map<FieldDto, FilterDto> filters,
                                                      Map<String, FieldMapper> availableFields) {
        Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices = new HashMap<>();
        filters.forEach((fieldDto, value) -> FindUtils.getFieldMapperByRemoteField(availableFields, fieldDto).values()
                .forEach(fieldMapper -> convertToAnotherRequest(filtersToAnotherServices, fieldDto, fieldMapper, value)));
        if (!filtersToAnotherServices.isEmpty()) {
            return getFilterResultFromOtherServices(filtersToAnotherServices);
        }

        return Collections.emptyMap();
    }

    private void convertToAnotherRequest(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices,
                                         FieldDto fieldDto,
                                         FieldMapper fieldMapper,
                                         FilterDto value) {
        String anotherServiceSearchField = fieldDto.getRemoteField();
        ClientLocalFieldObject clientLocalFieldObject = ClientLocalFieldObject.builder()
                .client(fieldMapper.getRemoteServiceClient())
                .fieldDto(fieldDto).build();
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

        String[] havingCondition = FindUtils.getHavingCondition(anotherServiceSearchField);
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

    private Map<FieldDto, Collection<Object>> getFilterResultFromOtherServices(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices) {
        Map<FieldDto, Collection<Object>> result = new HashMap<>();
        for (Map.Entry<ClientLocalFieldObject, FormDto> filtersService : filtersToAnotherServices.entrySet()) {
            Collection<Object> request = getRemoteResult(filtersService.getKey().getClient(), filtersService.getValue());
            if (request == null || request.isEmpty()) {
                return null;
            } else {
                FieldDto localFieldDto = filtersService.getKey().getFieldDto();
                if (result.get(localFieldDto) == null) {
                    result.put(localFieldDto, request);
                } else {
                    result.get(localFieldDto).addAll(request);
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
