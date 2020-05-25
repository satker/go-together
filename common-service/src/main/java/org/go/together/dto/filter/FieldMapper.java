package org.go.together.dto.filter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FieldMapper {
    private final String currentServiceField;
    private final String remoteServiceName;
    private final String remoteServiceMapping;
    private final String remoteServiceFieldGetter;
}
