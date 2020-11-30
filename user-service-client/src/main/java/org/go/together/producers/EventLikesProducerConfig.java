package org.go.together.producers;

import org.go.together.dto.EventLikeDto;
import org.go.together.kafka.config.producers.ProducerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.EVENT_LIKES;

@Component
public class EventLikesProducerConfig extends ProducerKafkaConfig<EventLikeDto> {
    @Override
    public String getConsumerId() {
        return EVENT_LIKES.getDescription();
    }
}
