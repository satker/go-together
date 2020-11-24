package org.go.together.config;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.enums.ServiceInfo;
import org.go.together.kafka.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Controller;

@Controller
public class GroupPhotoConsumerKafkaConfig extends ConsumerKafkaConfig<GroupPhotoDto> {
    @Override
    public String getConsumerId() {
        return ServiceInfo.GROUP_PHOTO_NAME.getDescription();
    }
}
