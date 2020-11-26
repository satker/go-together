package org.go.together.config;

import org.go.together.dto.EventLikeDto;
import org.go.together.enums.ServiceInfo;
import org.go.together.kafka.config.ConsumerKafkaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventLikesConsumerConfig extends ConsumerKafkaConfig<EventLikeDto> {
    @Override
    public String getConsumerId() {
        return ServiceInfo.EVENT_LIKES.getDescription();
    }
}
