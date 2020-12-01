package org.go.together.notification.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.kafka.NotificationEvent;
import org.go.together.kafka.NotificationEventStatus;
import org.go.together.notification.helpers.interfaces.KafkaSender;
import org.go.together.notification.helpers.interfaces.ReceiverSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReceiverAddSender implements ReceiverSender {
    private final KafkaSender kafkaSender;

    @Override
    public void send(UUID id, UUID producerId, UUID receiverId) {
        if (receiverId == null || producerId == null) {
            throw new IllegalArgumentException("Cannot send add receiver action to notification service: producerId or receiverId is null");
        }
        NotificationEvent receiverEvent = NotificationEvent.builder()
                .producerId(producerId)
                .status(NotificationEventStatus.ADD_RECEIVER)
                .receiverId(receiverId).build();
        kafkaSender.send(id, receiverEvent);
    }
}
