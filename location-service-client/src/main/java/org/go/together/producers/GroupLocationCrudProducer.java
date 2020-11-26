package org.go.together.producers;

import org.go.together.dto.GroupLocationDto;
import org.go.together.kafka.base.KafkaCrudClient;
import org.springframework.stereotype.Component;

@Component
public class GroupLocationCrudProducer extends KafkaCrudClient<GroupLocationDto> {
}
