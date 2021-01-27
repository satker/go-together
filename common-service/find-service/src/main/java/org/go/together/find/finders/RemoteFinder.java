package org.go.together.find.finders;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.node.FilterNode;
import org.go.together.find.finders.converter.RequestConverter;
import org.go.together.find.finders.request.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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

    public Collection<Object> getFilters(UUID requestId,
                                                        FilterNode filterNode,
                                                        Map<String, FieldMapper> availableFields) {
        Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices =
                getFieldMapperByRemoteField(availableFields, filterNode.getField()).stream()
                        .map(fieldMapper -> requestConverter.convert(filterNode, fieldMapper))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (!filtersToAnotherServices.isEmpty()) {
            Map.Entry<ClientLocalFieldObject, FormDto> remoteClientForm = filtersToAnotherServices.entrySet().iterator().next();
            return sender.send(requestId, remoteClientForm.getKey(), remoteClientForm.getValue());
        }

        return Collections.emptySet();
    }

    private Set<FieldMapper> getFieldMapperByRemoteField(Map<String, FieldMapper> availableFields,
                                                         FieldDto fieldDto) {
        String localEntityField = fieldDto.getPaths()[0];
        Set<String> singleGroupFields = Set.of(getSingleGroupFields(localEntityField));
        return availableFields.values().stream()
                .filter(fieldMapper -> fieldMapper.getRemoteServiceClient() != null)
                .filter(fieldMapper -> singleGroupFields.contains(fieldMapper.getCurrentServiceField()))
                .collect(Collectors.toSet());
    }
}
