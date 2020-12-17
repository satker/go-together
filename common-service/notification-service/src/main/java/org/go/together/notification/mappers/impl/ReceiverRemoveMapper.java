package org.go.together.notification.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.kafka.NotificationEvent;
import org.go.together.kafka.NotificationEventStatus;
import org.go.together.notification.mappers.interfaces.ReceiverMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReceiverRemoveMapper implements ReceiverMapper {
    //private final KafkaSender kafkaSender;

    @Override
    public NotificationEvent getNotificationEvent(UUID producerId, UUID receiverId) {
        if (receiverId == null || producerId == null) {
            throw new IllegalArgumentException("Cannot send remove receiver message to notification service: producerId or receiverId is null");
        }
        return NotificationEvent.builder()
                .producerId(producerId)
                .status(NotificationEventStatus.REMOVE_RECEIVER)
                .receiverId(receiverId).build();
        //kafkaSender.send(id, receiverEvent);
    }
}
