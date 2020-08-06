package org.go.together.notification;

import org.apache.commons.lang3.StringUtils;
import org.go.together.client.NotificationClient;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.enums.NotificationStatus;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.go.together.enums.NotificationStatus.CREATED;
import static org.go.together.enums.NotificationStatus.DELETED;
import static org.go.together.notification.utils.ComparatorUtils.compareDtos;
import static org.go.together.notification.utils.ComparatorUtils.getMainField;

@Component
public class NotificationServiceImpl<D extends Dto> implements NotificationService<D> {
    private NotificationClient notificationClient;

    @Autowired
    public void setNotificationClient(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void notificate(UUID id, D dto, String resultMessage, NotificationStatus notificationStatus) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> {
                    UUID ownerId = comparableDto.getOwnerId();
                    NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
                    notificationMessageDto.setMessage(resultMessage);
                    notificationMessageDto.setDate(new Date());
                    UUID producerId = Optional.ofNullable(comparableDto.getParentId()).orElse(id);
                    notificationClient.notificate(producerId, notificationStatus, notificationMessageDto);
                    if (notificationStatus == CREATED) {
                        addedReceiver(producerId, ownerId);
                    } else if (notificationStatus == DELETED) {
                        removedReceiver(producerId, ownerId);
                    }
                });
    }

    public String getMessage(D dto, D anotherDto, String serviceName, NotificationStatus notificationStatus) {
        return switch (notificationStatus) {
            case CREATED -> "Created " + serviceName + getMainField(dto) + ".";
            case UPDATED -> Optional.ofNullable(dto)
                    .map(id -> compareFields(dto, anotherDto, serviceName))
                    .orElse(null);
            case DELETED -> "Deleted " + serviceName + getMainField(dto) + ".";
        };
    }

    private String compareFields(D dto, D anotherDto, String serviceName) {
        Collection<String> result = new HashSet<>();
        compareDtos(result, serviceName, dto, anotherDto);
        return StringUtils.join(result, ".");
    }

    private void addedReceiver(UUID producerId, UUID receiverId) {
        if (receiverId != null) {
            notificationClient.addReceiver(producerId, receiverId);
        }
    }

    private void removedReceiver(UUID producerId, UUID receiverId) {
        notificationClient.removeReceiver(producerId, receiverId);
    }
}
