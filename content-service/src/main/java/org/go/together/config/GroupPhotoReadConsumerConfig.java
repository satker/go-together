package org.go.together.config;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.enums.ServiceInfo;
import org.go.together.kafka.config.consumers.ReadConsumerKafkaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupPhotoReadConsumerConfig extends ReadConsumerKafkaConfig<GroupPhotoDto> {
    @Override
    public String getConsumerId() {
        return ServiceInfo.GROUP_PHOTO_NAME.getDescription();
    }
}
