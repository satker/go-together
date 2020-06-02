package org.go.together.dto.filter;

import lombok.Builder;
import lombok.Getter;
import org.go.together.interfaces.FindClient;
import org.go.together.logic.CrudService;

@Builder
@Getter
public class FieldMapper {
    private final String currentServiceField;
    private final CrudService innerService;
    private final FindClient remoteServiceClient;
    private final String remoteServiceName;
    private final String remoteServiceFieldGetter;

    public String getPathRemoteFieldGetter() {
        return remoteServiceName + "." + remoteServiceFieldGetter;
    }
}
