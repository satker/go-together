package org.go.together.kafka.producer.beanpostprocessor;

import lombok.Builder;
import lombok.Getter;
import org.go.together.dto.Dto;
import org.go.together.kafka.producer.base.CrudClient;

@Builder
@Getter
public class ProducerRights {
    private final boolean isCreate;
    private final boolean isUpdate;
    private final boolean isValidate;
    private final boolean isRead;
    private final boolean isFind;
    private final boolean isDelete;
    private final CrudClient<? extends Dto> producer;
    private final String producerId;
}
