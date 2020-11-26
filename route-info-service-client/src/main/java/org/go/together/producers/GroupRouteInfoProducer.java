package org.go.together.producers;

import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.kafka.base.KafkaCrudClient;
import org.springframework.stereotype.Component;

@Component
public class GroupRouteInfoProducer extends KafkaCrudClient<GroupRouteInfoDto> {
}
