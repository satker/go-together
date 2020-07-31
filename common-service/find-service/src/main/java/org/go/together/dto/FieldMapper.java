package org.go.together.dto;

import lombok.Builder;
import lombok.Getter;
import org.go.together.FindService;
import org.go.together.client.FindClient;

@Builder
@Getter
public class FieldMapper {
    private final String currentServiceField;
    private final FindService innerService;
    private final FindClient remoteServiceClient;
    private final String remoteServiceName;
    private final String remoteServiceFieldGetter;

    public String getPathRemoteFieldGetter() {
        return remoteServiceName + "." + remoteServiceFieldGetter;
    }
}
