package org.go.together.producers;

import org.go.together.dto.EventLikeDto;
import org.go.together.kafka.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.EVENT_LIKES;

@Component
public class EventLikesProducer extends CrudClient<EventLikeDto> {
    @Override
    public String getConsumerId() {
        return EVENT_LIKES.getDescription();
    }
}
