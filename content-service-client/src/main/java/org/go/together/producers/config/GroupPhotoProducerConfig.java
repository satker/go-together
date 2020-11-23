package org.go.together.producers.config;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.kafka.config.ProducerKafkaConfig;
import org.springframework.context.annotation.Configuration;

import static org.go.together.enums.ServiceInfo.GROUP_PHOTO_NAME;

@Configuration
public class GroupPhotoProducerConfig extends ProducerKafkaConfig<GroupPhotoDto> {
    @Override
    public String getConsumerId() {
        return GROUP_PHOTO_NAME.getDescription();
    }
}
