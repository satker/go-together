package org.go.together.find.mapper;

import org.go.together.find.dto.FieldDto;

import static org.go.together.find.utils.FindUtils.getParsedRemoteField;

public class FieldMapperUtils {
    public static FieldDto getFieldDto(String searchField) {
        return FieldDto.builder()
                .remoteField(getAnotherServiceFilter(searchField))
                .localField(getParsedRemoteField(searchField)[0])
                .build();
    }

    public static String getAnotherServiceFilter(String string) {
        String[] otherServiceFields = getParsedRemoteField(string);
        if (otherServiceFields.length > 1) {
            return otherServiceFields[1];
        }
        return null;
    }
}
