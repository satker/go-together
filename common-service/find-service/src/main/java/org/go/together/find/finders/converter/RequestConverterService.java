package org.go.together.find.finders.converter;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.FilterDto;
import org.go.together.dto.FormDto;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.utils.FindUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestConverterService implements RequestConverter {
    public Map.Entry<ClientLocalFieldObject, FormDto> convert(
            Map.Entry<FieldDto, FilterDto> entry,
            FieldMapper fieldMapper) {
        String anotherServiceSearchField = entry.getKey().getRemoteField();
        FormDto remoteClientFormDto = getRemoteClientFormDto(anotherServiceSearchField, entry.getValue(), fieldMapper);
        ClientLocalFieldObject clientLocalFieldObject = ClientLocalFieldObject.builder()
                .client(fieldMapper.getRemoteServiceClient())
                .mainIdFeild(remoteClientFormDto.getMainIdField())
                .fieldDto(entry.getKey()).build();
        return Map.entry(clientLocalFieldObject, remoteClientFormDto);
    }

    private FormDto getRemoteClientFormDto(String anotherServiceSearchField,
                                           FilterDto value,
                                           FieldMapper fieldMapper) {
        FieldDto remoteFieldDto = getRemoteMainAndGetField(anotherServiceSearchField, fieldMapper);
        Map<String, FilterDto> map = new HashMap<>();
        map.put(remoteFieldDto.getLocalField(), value);
        return new FormDto(null, map, remoteFieldDto.getRemoteField());
    }

    private FieldDto getRemoteMainAndGetField(String anotherServiceSearchField, FieldMapper fieldMapper) {
        String remoteGetField = fieldMapper.getRemoteServiceFieldGetter();

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
