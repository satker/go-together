package org.go.together.producers.config;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.kafka.config.producers.CreateProducerKafkaConfig;
import org.springframework.context.annotation.Configuration;

import static org.go.together.enums.ServiceInfo.GROUP_PHOTO_NAME;

@Configuration
public class GroupPhotoCreateProducerConfig extends CreateProducerKafkaConfig<GroupPhotoDto> {
    @Override
    public String getConsumerId() {
        return GROUP_PHOTO_NAME.getDescription();
    }
}
