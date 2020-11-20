package org.go.together.config;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.enums.ServiceInfo;
import org.go.together.kafka.config.consumers.ChangeConsumerKafkaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupPhotoChangeConsumerConfig extends ChangeConsumerKafkaConfig<GroupPhotoDto> {
    @Override
    protected String getConsumerId() {
        return ServiceInfo.GROUP_PHOTO_NAME.getDescription();
    }
}
