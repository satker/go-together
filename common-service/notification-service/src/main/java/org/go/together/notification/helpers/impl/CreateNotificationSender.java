package org.go.together.notification.helpers.impl;

import org.go.together.compare.ComparableDto;
import org.go.together.dto.Dto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.message.NotificationEvent;
import org.go.together.message.NotificationEventStatus;
import org.go.together.notification.helpers.interfaces.NotificationSender;
import org.go.together.notification.helpers.interfaces.ReceiverSender;
import org.go.together.notification.streams.NotificationSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreateNotificationSender implements NotificationSender {
    private final NotificationSource source;
    private final ReceiverSender receiverSender;

    public CreateNotificationSender(NotificationSource source,
                                    @Qualifier("receiverAddSender") ReceiverSender receiverSender) {
        this.source = source;
        this.receiverSender = receiverSender;
    }

    @Override
    public <D extends Dto> void send(UUID id, D dto, String resultMessage) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> sendCreateMessage(id, resultMessage, comparableDto));
    }

    private void sendCreateMessage(UUID id, String resultMessage, ComparableDto comparableDto) {
        UUID ownerId = comparableDto.getOwnerId();
        UUID producerId = Optional.ofNullable(comparableDto.getParentId()).orElse(id);
        NotificationMessageDto notificationMessageDto = NotificationSender.getNotificationMessageDto(resultMessage);
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .message(notificationMessageDto)
                .status(NotificationEventStatus.CREATE_MESSAGE)
                .producerId(producerId).build();
        source.output().send(MessageBuilder.withPayload(notificationEvent).build());
        receiverSender.send(producerId, ownerId);
    }
}
