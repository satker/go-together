package org.go.together.logic.services;

import org.apache.commons.lang3.StringUtils;
import org.go.together.CustomRepository;
import org.go.together.client.NotificationClient;
import org.go.together.dto.NotificationStatus;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.go.together.utils.ComparatorUtils.compareDtos;
import static org.go.together.utils.ComparatorUtils.getMainField;

public abstract class NotificationService<D extends Dto, E extends IdentifiedEntity> extends FindService<E> {
    @Autowired
    private NotificationClient notificationClient;

    protected NotificationService(CustomRepository<E> repository) {
        super(repository);
    }

    protected void notificate(UUID id, D dto, String resultMessage, NotificationStatus notificationStatus) {
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
                        .map(D::getOwnerId)
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
        }
    }

    protected void removedReceiver(UUID producerId, UUID receiverId) {
        notificationClient.removeReceiver(producerId, receiverId);
    }

    abstract D read(UUID uuid);
}
