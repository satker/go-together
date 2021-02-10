package org.go.together.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.NotificationService;
import org.go.together.compare.ComparableDto;
import org.go.together.dto.Dto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.enums.NotificationStatus;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.mappers.interfaces.NotificationMapper;
import org.go.together.notification.mappers.interfaces.ReceiverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class CommonNotificationService<D extends Dto> implements NotificationService<D> {
    private final static Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationMapper notificationMapper;
    private final ReceiverMapper receiverMapper;
    private final Comparator<D> dtoComparator;
    private final CrudProducer<NotificationMessageDto> notificationMessageProducer;
    private final CrudProducer<NotificationReceiverDto> notificationReceiverProducer;
    private final ObjectMapper objectMapper;

    public CommonNotificationService(NotificationMapper notificationMapper,
                                     ReceiverMapper receiverMapper,
                                     Comparator<D> dtoComparator,
                                     CrudProducer<NotificationMessageDto> notificationMessageProducer,
                                     CrudProducer<NotificationReceiverDto> notificationReceiverProducer) {
        this.notificationMapper = notificationMapper;
        this.notificationMessageProducer = notificationMessageProducer;
        this.notificationReceiverProducer = notificationReceiverProducer;
        this.receiverMapper = receiverMapper;
        this.dtoComparator = dtoComparator;
        this.objectMapper = new ObjectMapper();
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
        Optional.ofNullable(notificationMapper.getNotificationDto(id, dto, resultMessage))
                .ifPresent(notificationDto -> {
                    UUID ownerId = ((ComparableDto) dto).getOwnerId();
                    NotificationReceiverDto receiverDto =
                            receiverMapper.getReceiverDto(notificationDto.getNotification().getProducerId(),
                                    ownerId);
                    notificationReceiverProducer.create(requestId, receiverDto);
                    log.info(requestId + ". Creation receiver owner by id = '" + ownerId + "' successful.");
                    notificationMessageProducer.create(requestId, notificationDto);
                    log.info(requestId + ". Added message = '" + resultMessage + "' successful.");
                });
    }

    @Override
    public void updateNotification(UUID requestId, UUID id, D dto, String resultMessage) {
        Optional.ofNullable(notificationMapper.getNotificationDto(id, dto, resultMessage))
                .ifPresent(notificationMessageDto -> {
                    notificationMessageProducer.create(requestId, notificationMessageDto);
                    log.info(requestId + ". Added message = '" + resultMessage + "' successful.");
                });
    }

    @SneakyThrows
    private String compareFields(D originalDto, D changedDto, String serviceName) {
        Map<String, Object> compareResult = dtoComparator.compare(serviceName, originalDto, changedDto);
        return objectMapper.writeValueAsString(compareResult);
    }


    @Override
    public void removeReceiver(UUID requestId, D dto) {
        Optional.ofNullable(dto)
                .map(d -> (ComparableDto) d)
                .ifPresent(comparableDto -> {
                    UUID ownerId = comparableDto.getOwnerId();
                    notificationReceiverProducer.delete(requestId, ownerId);
                    log.info(requestId + ". Delete receiver = '" + ownerId + "' successful.");
                });
    }
}
