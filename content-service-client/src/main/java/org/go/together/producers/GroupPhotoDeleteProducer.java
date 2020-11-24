package org.go.together.producers;

import org.go.together.kafka.impl.producers.CommonDeleteKafkaProducer;
import org.springframework.stereotype.Component;

import static org.go.together.enums.ServiceInfo.GROUP_PHOTO_NAME;

@Component
public class GroupPhotoDeleteProducer extends CommonDeleteKafkaProducer {
    @Override
    public String getTopicId() {
        return GROUP_PHOTO_NAME.getDescription();
    }
}
