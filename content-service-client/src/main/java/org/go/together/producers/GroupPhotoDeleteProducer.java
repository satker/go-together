package org.go.together.producers;

import org.go.together.kafka.impl.producers.CommonDeleteKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.go.together.enums.ServiceInfo.GROUP_PHOTO_NAME;

@Component
public class GroupPhotoDeleteProducer extends CommonDeleteKafkaProducer {
    @Override
    @Autowired
    public void setDeleteKafkaTemplate(@Qualifier("groupPhotosDeleteKafkaTemplate")
                                               KafkaTemplate<UUID, UUID> kafkaTemplate) {
        super.setDeleteKafkaTemplate(kafkaTemplate);
    }

    @Override
    public String getTopicId() {
        return GROUP_PHOTO_NAME.getDescription();
    }
}
