package org.go.together.compare;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.go.together.base.FindService;
import org.go.together.kafka.producers.FindProducer;

@Builder
@Getter
@EqualsAndHashCode
public class FieldMapper {
    private final String currentServiceField;
    private final FindService<?> innerService;
    private final FindProducer remoteServiceClient;
    private final String remoteServiceName;
    private final String remoteServiceFieldGetter;
    private final Class<?> fieldClass;

    public String getPathRemoteFieldGetter() {
        return remoteServiceName + "." + remoteServiceFieldGetter;
    }
}
