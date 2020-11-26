package org.go.together.producers;

import org.go.together.dto.EventLikeDto;
import org.go.together.kafka.base.KafkaCrudClient;
import org.springframework.stereotype.Component;

@Component
public class EventLikesProducer extends KafkaCrudClient<EventLikeDto> {
}
