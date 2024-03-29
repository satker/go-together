package org.go.together.producers;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.EventLikeDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.EVENT_LIKES;

@Component
@EnableAutoConfigurationKafkaProducer(producerId = EVENT_LIKES)
public class EventLikesProducer extends CrudClient<EventLikeDto> {
}
