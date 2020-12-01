package org.go.together.config;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.enums.ContentServiceInfo;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

@Component
public class GroupPhotoConsumerKafkaConfig extends ConsumerKafkaConfig<GroupPhotoDto> {
    @Override
    public String getConsumerId() {
        return ContentServiceInfo.GROUP_PHOTO_NAME.getDescription();
    }
}
