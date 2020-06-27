package org.go.together.logic.services;

import org.apache.commons.lang3.StringUtils;
import org.go.together.CustomRepository;
import org.go.together.client.NotificationClient;
import org.go.together.dto.NotificationStatus;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.go.together.utils.ComparatorUtils.compareDtos;
import static org.go.together.utils.ComparatorUtils.getMainField;

public abstract class NotificationService<D extends Dto, E extends IdentifiedEntity> extends FindService<E> {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NotificationClient notificationClient;

    protected NotificationService(CustomRepository<E> repository) {
        super(repository);
    }

    protected void notificate(UUID id, D dto, String resultMessage, NotificationStatus notificationStatus) {
        log.info(getServiceName() + " dto with " + id + ": " + resultMessage);
        Optional.ofNullable(dto)
                .map(D::getOwnerId)
                .ifPresent(ownerId -> {
                    notificationClient.notificate(id, notificationStatus, resultMessage);
                    if (notificationStatus == NotificationStatus.CREATED) {
                        addedReceiver(id, ownerId);
                    }
                });
    }

    protected String getMessage(D dto, NotificationStatus notificationStatus) {
        switch (notificationStatus) {
            case CREATED:
                return "Created " + getServiceName() + getMainField(dto) + ".";
            case UPDATED:
                return Optional.ofNullable(dto)
                        .map(id -> compareFields(dto))
                        .orElse(null);
            case DELETED:
                return "Deleted " + getServiceName() + getMainField(dto) + ".";
            default:
                return null;
        }
    }

    protected String compareFields(D anotherDto) {
        D dto = read(anotherDto.getId());
        Collection<String> result = new HashSet<>();
        compareDtos(result, getServiceName(), dto, anotherDto);
        return StringUtils.join(result, ".");
    }

    protected void addedReceiver(UUID producerId, UUID receiverId) {
        if (receiverId != null) {
            notificationClient.addReceiver(producerId, receiverId);
            log.info(getServiceName() + " dto producer with id " + producerId + ": added receiver " + receiverId);
        }
    }

    protected void removedReceiver(String simpleClassName, UUID producerId, UUID receiverId) {
        notificationClient.removeReceiver(producerId, receiverId);
        log.info(getServiceName() + " dto producer with id " + producerId + ": removed receiver " + receiverId);
    }

    abstract D read(UUID uuid);
}
