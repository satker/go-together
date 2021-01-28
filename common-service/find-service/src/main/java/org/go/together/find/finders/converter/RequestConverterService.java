package org.go.together.find.finders.converter;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FilterDto;
import org.go.together.dto.FilterValueDto;
import org.go.together.dto.FormDto;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.node.FilterNode;
import org.go.together.find.utils.FindUtils;
import org.go.together.kafka.producers.FindProducer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class RequestConverterService implements RequestConverter {
    public Pair<FindProducer<?>, FormDto> convert(FilterNode filterNode, FieldMapper fieldMapper) {
        String anotherServiceSearchField = filterNode.getField().getRemoteField();
        FormDto remoteClientFormDto = getRemoteClientFormDto(anotherServiceSearchField, filterNode, fieldMapper);
        return Pair.of(fieldMapper.getRemoteServiceClient(), remoteClientFormDto);
    }

    private FormDto getRemoteClientFormDto(String anotherServiceSearchField,
                                           FilterNode filterNode,
                                           FieldMapper fieldMapper) {
        FieldDto remoteFieldDto = getRemoteMainAndGetField(anotherServiceSearchField, fieldMapper);
        FilterDto filterDto = new FilterDto(Collections.singleton(castValuesToMap(filterNode.getValues().getValue())));
        Map<String, FilterDto> map = Map.of(remoteFieldDto.getLocalField(), filterDto);
        return new FormDto(null, map, remoteFieldDto.getRemoteField());
    }

    private Map<String, FilterValueDto> castValuesToMap(Object value) {
        if (value instanceof Map) {
            return (Map<String, FilterValueDto>) value;
        }
        throw new IncorrectFindObject("Incorrect remote search values");
    }

    private FieldDto getRemoteMainAndGetField(String anotherServiceSearchField, FieldMapper fieldMapper) {
        String remoteGetField = fieldMapper.getRemoteServiceName() + "." + fieldMapper.getRemoteServiceFieldGetter();

        String[] havingCondition = FindUtils.getHavingCondition(anotherServiceSearchField);
        if (havingCondition.length > 1) {
            try {
                int havingNumber = Integer.parseInt(havingCondition[1]);
                remoteGetField = remoteGetField + ":" + havingNumber;
                return new FieldDto(havingCondition[0], remoteGetField);
            } catch (NumberFormatException e) {
                throw new IncorrectFindObject("Incorrect having condition: " + havingCondition[1]);
            }
        }
        return new FieldDto(anotherServiceSearchField, remoteGetField);
    }
}
