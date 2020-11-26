package org.go.together.producers;

import org.go.together.dto.EventLikeDto;
import org.go.together.kafka.config.ProducerKafkaConfig;
import org.springframework.context.annotation.Configuration;

import static org.go.together.enums.ServiceInfo.EVENT_LIKES;

@Configuration
public class EventLikesProducerConfig extends ProducerKafkaConfig<EventLikeDto> {
    @Override
    public String getConsumerId() {
        return EVENT_LIKES.getDescription();
    }
}
