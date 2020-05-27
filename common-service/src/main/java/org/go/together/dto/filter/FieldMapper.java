package org.go.together.dto.filter;

import lombok.Builder;
import lombok.Getter;
import org.go.together.interfaces.FindClient;

@Builder
@Getter
public class FieldMapper {
    private final String currentServiceField;
    private final FindClient remoteServiceClient;
    private final String remoteServiceName;
    private final String remoteServiceFieldGetter;
}
