package org.go.together.compare;

import lombok.Builder;
import lombok.Getter;
import org.go.together.base.FindClient;
import org.go.together.base.FindService;

@Builder
@Getter
public class FieldMapper {
    private final String currentServiceField;
    private final FindService<?> innerService;
    private final FindClient remoteServiceClient;
    private final String remoteServiceName;
    private final String remoteServiceFieldGetter;
    private final Class<?> fieldClass;

    public String getPathRemoteFieldGetter() {
        return remoteServiceName + "." + remoteServiceFieldGetter;
    }
}
