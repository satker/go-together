package org.go.together.config;

import org.go.together.dto.EventLikeDto;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.EVENT_LIKES;

@Component
public class EventLikesConsumerConfig extends ConsumerKafkaConfig<EventLikeDto> {
    @Override
    public String getConsumerId() {
        return EVENT_LIKES.getDescription();
    }
}
