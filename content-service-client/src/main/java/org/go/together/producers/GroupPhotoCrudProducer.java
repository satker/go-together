package org.go.together.producers;

import org.go.together.dto.GroupPhotoDto;
import org.go.together.kafka.base.KafkaCrudClient;
import org.springframework.stereotype.Component;

@Component
public class GroupPhotoCrudProducer extends KafkaCrudClient<GroupPhotoDto> {
}
