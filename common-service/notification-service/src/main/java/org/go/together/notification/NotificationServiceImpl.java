package org.go.together.notification;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.enums.NotificationStatus;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NotificationService;
import org.go.together.message.NotificationEvent;
import org.go.together.message.NotificationEventStatus;
import org.go.together.notification.streams.NotificationSource;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.go.together.notification.utils.ComparatorUtils.compareDtos;
import static org.go.together.notification.utils.ComparatorUtils.getMainField;

@Component
@RequiredArgsConstructor
@EnableBinding(NotificationSource.class)
public class NotificationServiceImpl<D extends Dto> implements NotificationService<D> {
    private final NotificationSource source;

    public void createNotification(UUID id, D dto, String resultMessage) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> {
                    UUID ownerId = comparableDto.getOwnerId();
                    UUID producerId = Optional.ofNullable(comparableDto.getParentId()).orElse(id);
                    NotificationMessageDto notificationMessageDto = getNotificationMessageDto(resultMessage);
                    NotificationEvent notificationEvent = NotificationEvent.builder()
                            .message(notificationMessageDto)
                            .status(NotificationEventStatus.CREATE_MESSAGE)
                            .producerId(producerId).build();
                    source.output().send(MessageBuilder.withPayload(notificationEvent).build());
                    addedReceiver(producerId, ownerId);
                });
    }

    public void updateNotification(UUID id, D dto, String resultMessage) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> {
                    UUID producerId = Optional.ofNullable(comparableDto.getParentId()).orElse(id);
                    NotificationMessageDto notificationMessageDto = getNotificationMessageDto(resultMessage);
                    NotificationEvent notificationEvent = NotificationEvent.builder()
                            .message(notificationMessageDto)
                            .status(NotificationEventStatus.UPDATE_MESSAGE)
                            .producerId(producerId).build();
                    source.output().send(MessageBuilder.withPayload(notificationEvent).build());
                });
    }

    private NotificationMessageDto getNotificationMessageDto(String resultMessage) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(resultMessage);
        notificationMessageDto.setDate(new Date());
        return notificationMessageDto;
    }

    public String getMessage(D originalDto, D changedDto, String serviceName, NotificationStatus notificationStatus) {
        return switch (notificationStatus) {
            case CREATED -> "Created " + serviceName + getMainField(originalDto) + ".";
            case UPDATED -> Optional.ofNullable(originalDto)
                    .map(id -> compareFields(originalDto, changedDto, serviceName))
                    .orElse(null);
            case DELETED -> "Deleted " + serviceName + getMainField(originalDto) + ".";
        };
    }

    private String compareFields(D originalDto, D changedDto, String serviceName) {
        Collection<String> result = new HashSet<>();
        compareDtos(result, serviceName, originalDto, changedDto);
        return StringUtils.join(result, ".");
    }

    private void addedReceiver(UUID producerId, UUID receiverId) {
        if (receiverId != null) {
            NotificationEvent receiverEvent = NotificationEvent.builder()
                    .producerId(producerId)
                    .status(NotificationEventStatus.ADD_RECEIVER)
                    .receiverId(receiverId).build();
            source.output().send(MessageBuilder.withPayload(receiverEvent).build());
        }
    }

    public void removeReceiver(D dto) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> {
                    UUID ownerId = comparableDto.getOwnerId();
                    Optional.ofNullable(comparableDto.getParentId())
                            .map(producerId -> NotificationEvent.builder()
                                    .producerId(producerId)
                                    .status(NotificationEventStatus.REMOVE_RECEIVER)
                                    .receiverId(ownerId).build())
                            .ifPresent((event) -> source.output().send(MessageBuilder.withPayload(event).build()));
                });
    }
}
