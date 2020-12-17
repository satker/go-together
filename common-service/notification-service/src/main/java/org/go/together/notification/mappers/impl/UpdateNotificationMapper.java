package org.go.together.notification.mappers.impl;

import org.go.together.compare.ComparableDto;
import org.go.together.dto.Dto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.kafka.NotificationEvent;
import org.go.together.kafka.NotificationEventStatus;
import org.go.together.notification.mappers.interfaces.NotificationMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
//@RequiredArgsConstructor
public class UpdateNotificationMapper implements NotificationMapper {
    //private final KafkaSender kafkaSender;

    @Override
    public <D extends Dto> NotificationEvent getNotificationEvent(UUID id, D dto, String resultMessage) {
        return Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .map(comparableDto -> getEvent(id, resultMessage, comparableDto)).orElse(null);
    }

    private NotificationEvent getEvent(UUID id, String resultMessage, ComparableDto comparableDto) {
        UUID producerId = Optional.ofNullable(comparableDto.getParentId()).orElse(id);
        NotificationMessageDto notificationMessageDto = getNotificationMessageDto(resultMessage);
        return NotificationEvent.builder()
                .message(notificationMessageDto)
                .status(NotificationEventStatus.UPDATE_MESSAGE)
                .producerId(producerId).build();
    }
}
