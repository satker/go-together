package org.go.together.producers;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.ContentServiceInfo.GROUP_PHOTO_NAME;

@Component
public class GroupPhotoCrudProducer extends CrudClient<GroupPhotoDto> {
    @Override
    public String getConsumerId() {
        return GROUP_PHOTO_NAME.getDescription();
    }
}
