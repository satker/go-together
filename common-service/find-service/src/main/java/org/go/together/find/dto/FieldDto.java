package org.go.together.find.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static org.go.together.find.utils.FindUtils.getPathFields;

@Builder
@Getter
@EqualsAndHashCode
public class FieldDto {
    private final String localField;
    private final String remoteField;

    public String[] getPaths(){
        return getPathFields(localField);
    }

    @Override
    public String toString() {
        return localField;
    }
}
