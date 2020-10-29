package org.go.together.notification.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.message.NotificationEvent;
import org.go.together.message.NotificationEventStatus;
import org.go.together.notification.helpers.interfaces.ReceiverSender;
import org.go.together.notification.streams.NotificationSource;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReceiverAddSender implements ReceiverSender {
    private final NotificationSource source;

    @Override
    public void send(@NotNull UUID producerId, @NotNull UUID receiverId) {
        if (receiverId == null || producerId == null) {
            throw new IllegalArgumentException("Cannot send add receiver action to notification service: producerId or receiverId is null");
        }
        NotificationEvent receiverEvent = NotificationEvent.builder()
                .producerId(producerId)
                .status(NotificationEventStatus.ADD_RECEIVER)
                .receiverId(receiverId).build();
        source.output().send(MessageBuilder.withPayload(receiverEvent).build());
    }
}
