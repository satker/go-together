package org.go.together.logic.services;

import org.apache.commons.lang3.StringUtils;
import org.go.together.CustomRepository;
import org.go.together.client.NotificationClient;
import org.go.together.dto.NotificationStatus;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.go.together.utils.ComparatorUtils.compareDtos;

public abstract class NotificationService<D extends Dto, E extends IdentifiedEntity> extends FindService<E> {
    @Autowired
    private NotificationClient notificationClient;

    private final CustomRepository<E> repository;
    private final Mapper<D, E> mapper;

    protected NotificationService(CustomRepository<E> repository,
                                  Mapper<D, E> mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    protected void notificate(UUID id, D dto, NotificationStatus notificationStatus) {
        String resultMessage = null;
        if (notificationStatus == NotificationStatus.CREATED) {
            resultMessage = "Created " + getServiceName() + ".";
        } else if (notificationStatus == NotificationStatus.DELETED) {
            resultMessage = "Deleted " + getServiceName() + ".";
        } else if (notificationStatus == NotificationStatus.UPDATED) {
            resultMessage = compareFields(dto);
        }
        if (dto.getOwnerId() != null) {
            notificationClient.notificate(id, notificationStatus, resultMessage);
        }
        if (notificationStatus == NotificationStatus.CREATED) {
            addedReceiver(id, dto.getOwnerId());
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

    public D read(UUID uuid) {
        Optional<E> entityById = repository.findById(uuid);
        return entityById.map(mapper::entityToDto).orElse(null);
    }
}
