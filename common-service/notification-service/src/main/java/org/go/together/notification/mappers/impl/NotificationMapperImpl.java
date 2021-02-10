package org.go.together.notification.mappers.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.compare.ComparableDto;
import org.go.together.dto.Dto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.notification.mappers.interfaces.NotificationMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class NotificationMapperImpl implements NotificationMapper {
    private static final String EMPTY_MESSAGE = "{}";

    @Override
    public <D extends Dto> NotificationMessageDto getNotificationDto(UUID id, D dto, String resultMessage) {
        return Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .map(comparableDto -> getNotificationMessageDto(id, resultMessage, comparableDto)).orElse(null);
    }

    private NotificationMessageDto getNotificationMessageDto(UUID id, String resultMessage, ComparableDto comparableDto) {
        if (StringUtils.isBlank(resultMessage) || resultMessage.equals(EMPTY_MESSAGE)) {
            return null;
        }
        UUID producerId = Optional.ofNullable(comparableDto.getParentId()).orElse(id);
        NotificationMessageDto notificationMessageDto = getNotificationMessageDto(resultMessage);
        enrichByNotificationDto(producerId, notificationMessageDto);
        return notificationMessageDto;
    }

    private void enrichByNotificationDto(UUID producerId, NotificationMessageDto notificationMessageDto) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(producerId);
        notificationMessageDto.setNotification(notificationDto);
    }

    private NotificationMessageDto getNotificationMessageDto(String resultMessage) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(resultMessage);
        notificationMessageDto.setDate(new Date());
        return notificationMessageDto;
    }

}
