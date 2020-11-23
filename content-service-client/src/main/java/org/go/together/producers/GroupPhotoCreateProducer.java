package org.go.together.producers;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.impl.producers.CommonCreateKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.go.together.enums.ServiceInfo.GROUP_PHOTO_NAME;

@Component
public class GroupPhotoCreateProducer extends CommonCreateKafkaProducer<GroupPhotoDto> {
    @Override
    public String getTopicId() {
        return GROUP_PHOTO_NAME.getDescription();
    }

    @Override
    @Autowired
    public void setCreateReplyingKafkaTemplate(@Qualifier("groupPhotosCreateReplyingKafkaTemplate")
                                                       ReplyingKafkaTemplate<UUID, GroupPhotoDto, IdDto> kafkaTemplate) {
        super.setCreateReplyingKafkaTemplate(kafkaTemplate);
    }
}
