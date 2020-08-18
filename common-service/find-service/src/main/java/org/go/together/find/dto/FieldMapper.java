package org.go.together.find.dto;

import lombok.Builder;
import lombok.Getter;
import org.go.together.find.FindService;
import org.go.together.find.client.FindClient;

@Builder
@Getter
public class FieldMapper {
    private final String currentServiceField;
    private final FindService innerService;
    private final FindClient remoteServiceClient;
    private final String remoteServiceName;
    private final String remoteServiceFieldGetter;
    private final Class fieldClass;

    public String getPathRemoteFieldGetter() {
        return remoteServiceName + "." + remoteServiceFieldGetter;
    }
}
