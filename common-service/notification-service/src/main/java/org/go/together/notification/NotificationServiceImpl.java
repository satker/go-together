package org.go.together.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.go.together.enums.NotificationStatus;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NotificationService;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.helpers.interfaces.NotificationSender;
import org.go.together.notification.helpers.interfaces.ReceiverSender;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class NotificationServiceImpl<D extends Dto> implements NotificationService<D> {
    private final NotificationSender createNotificationSender;
    private final NotificationSender updateNotificationSender;
    private final ReceiverSender removeReceiverSender;
    private final Comparator<D> dtoComparator;

    public NotificationServiceImpl(@Qualifier("createNotificationSender") NotificationSender createNotificationSender,
                                   @Qualifier("updateNotificationSender") NotificationSender updateNotificationSender,
                                   @Qualifier("receiverRemoveSender") ReceiverSender removeReceiverSender,
                                   Comparator<D> dtoComparator) {
        this.createNotificationSender = createNotificationSender;
        this.updateNotificationSender = updateNotificationSender;
        this.removeReceiverSender = removeReceiverSender;
        this.dtoComparator = dtoComparator;
    }

    public String getMessage(D originalDto, D changedDto, String serviceName, NotificationStatus notificationStatus) {
        return switch (notificationStatus) {
            case CREATED -> "Created " + serviceName + ((ComparableDto) originalDto).getMainField() + ".";
            case UPDATED -> Optional.ofNullable(originalDto)
                    .map(id -> compareFields(originalDto, changedDto, serviceName))
                    .orElse(null);
            case DELETED -> "Deleted " + serviceName + ((ComparableDto) originalDto).getMainField() + ".";
        };
    }

    @Override
    public void createNotification(UUID id, D dto, String resultMessage) {
        createNotificationSender.send(id, dto, resultMessage);
    }

    @Override
    public void updateNotification(UUID id, D dto, String resultMessage) {
        updateNotificationSender.send(id, dto, resultMessage);
    }

    @SneakyThrows
    private String compareFields(D originalDto, D changedDto, String serviceName) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dtoComparator.compare(serviceName, originalDto, changedDto));
    }


    @Override
    public void removeReceiver(D dto) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> {
                    UUID ownerId = comparableDto.getOwnerId();
                    Optional.ofNullable(comparableDto.getParentId())
                            .ifPresent(producerId -> removeReceiverSender.send(producerId, ownerId));
                });
    }
}
