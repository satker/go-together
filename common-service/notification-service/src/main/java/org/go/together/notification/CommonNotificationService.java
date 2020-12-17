package org.go.together.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.NotificationService;
import org.go.together.compare.ComparableDto;
import org.go.together.dto.Dto;
import org.go.together.enums.NotificationStatus;
import org.go.together.kafka.NotificationEvent;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.senders.interfaces.KafkaSender;
import org.go.together.notification.mappers.interfaces.NotificationMapper;
import org.go.together.notification.mappers.interfaces.ReceiverMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CommonNotificationService<D extends Dto> implements NotificationService<D> {
    private final NotificationMapper createNotificationMapper;
    private final NotificationMapper updateNotificationMapper;
    private final KafkaSender kafkaSender;
    private final ReceiverMapper removeReceiverMapper;
    private final ReceiverMapper addReceiverMapper;
    private final Comparator<D> dtoComparator;

    public CommonNotificationService(@Qualifier("createNotificationMapper") NotificationMapper createNotificationMapper,
                                     @Qualifier("updateNotificationMapper") NotificationMapper updateNotificationMapper,
                                     KafkaSender kafkaSender,
                                     @Qualifier("receiverRemoveMapper") ReceiverMapper removeReceiverMapper,
                                     @Qualifier("receiverAddMapper") ReceiverMapper addReceiverMapper,
                                     Comparator<D> dtoComparator) {
        this.createNotificationMapper = createNotificationMapper;
        this.updateNotificationMapper = updateNotificationMapper;
        this.kafkaSender = kafkaSender;
        this.removeReceiverMapper = removeReceiverMapper;
        this.addReceiverMapper = addReceiverMapper;
        this.dtoComparator = dtoComparator;
    }

    public String getMessage(D originalDto, D changedDto, String serviceName, NotificationStatus notificationStatus) {
        return switch (notificationStatus) {
            case CREATED -> "Created " + serviceName + StringUtils.SPACE +
                    ((ComparableDto) originalDto).getMainField() + ".";
            case UPDATED -> Optional.ofNullable(originalDto)
                    .map(id -> compareFields(originalDto, changedDto, serviceName))
                    .orElse(null);
            case DELETED -> "Deleted " + serviceName + StringUtils.SPACE +
                    ((ComparableDto) originalDto).getMainField() + ".";
        };
    }

    @Override
    public void createNotification(UUID requestId, UUID id, D dto, String resultMessage) {
        Optional.ofNullable(createNotificationMapper.getNotificationEvent(id, dto, resultMessage))
                .ifPresent(event -> {
                    NotificationEvent addReceiverEvent =
                            addReceiverMapper.getNotificationEvent(event.getProducerId(), ((ComparableDto) dto).getOwnerId());
                    kafkaSender.send(requestId, addReceiverEvent);
                    kafkaSender.send(requestId, event);
                });
    }

    @Override
    public void updateNotification(UUID requestId, UUID id, D dto, String resultMessage) {
        Optional.ofNullable(updateNotificationMapper.getNotificationEvent(id, dto, resultMessage))
                .ifPresent(event -> kafkaSender.send(requestId, event));
    }

    @SneakyThrows
    private String compareFields(D originalDto, D changedDto, String serviceName) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dtoComparator.compare(serviceName, originalDto, changedDto));
    }


    @Override
    public void removeReceiver(UUID requestId, D dto) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> {
                    UUID ownerId = comparableDto.getOwnerId();
                    Optional.ofNullable(comparableDto.getParentId())
                            .ifPresent(producerId -> {
                                NotificationEvent notificationEvent = removeReceiverMapper.getNotificationEvent(producerId, ownerId);
                                kafkaSender.send(requestId, notificationEvent);
                            });
                });
    }
}
