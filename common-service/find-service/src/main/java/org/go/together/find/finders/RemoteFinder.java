package org.go.together.find.finders;

import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.finders.converter.RequestConverter;
import org.go.together.find.finders.request.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.go.together.find.utils.FindUtils.getSingleGroupFields;

@Component
public class RemoteFinder implements Finder {
    private RequestConverter requestConverter;
    private Sender sender;

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @Autowired
    public void setRequestConverter(RequestConverter requestConverter) {
        this.requestConverter = requestConverter;
    }

    public Map<FieldDto, Collection<Object>> getFilters(Map<FieldDto, FilterDto> filters,
                                                        Map<String, FieldMapper> availableFields) {
        Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices = filters.entrySet().parallelStream()
                .flatMap(entry -> getFieldMapperByRemoteField(availableFields, entry.getKey()).stream()
                        .map(fieldMapper -> requestConverter.convert(entry, fieldMapper)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, this::resolveRemoteFormDtos));
        if (!filtersToAnotherServices.isEmpty()) {
            return sender.send(filtersToAnotherServices);
        }

        return Collections.emptyMap();
    }

    private FormDto resolveRemoteFormDtos(FormDto formDto1, FormDto formDto2) {
        formDto1.getFilters().putAll(formDto2.getFilters());
        return formDto1;
    }

    private Set<FieldMapper> getFieldMapperByRemoteField(Map<String, FieldMapper> availableFields,
                                                         FieldDto fieldDto) {
        String localEntityField = fieldDto.getPaths()[0];
        Set<String> singleGroupFields = Set.of(getSingleGroupFields(localEntityField));
        return availableFields.values().stream()
                .filter(fieldMapper -> singleGroupFields.contains(fieldMapper.getCurrentServiceField()))
                .collect(Collectors.toSet());
    }
}
