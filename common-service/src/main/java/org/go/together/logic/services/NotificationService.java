package org.go.together.logic.services;

import org.apache.commons.lang3.StringUtils;
import org.go.together.CustomRepository;
import org.go.together.client.NotificationClient;
import org.go.together.enums.CrudOperation;
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
    private final CustomRepository<E> repository;
    private final Mapper<D, E> mapper;
    @Autowired
    private NotificationClient notificationClient;

    protected NotificationService(CustomRepository<E> repository,
                                  Mapper<D, E> mapper) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
    }

    protected void notificate(UUID id, String message, CrudOperation crudOperation) {
        String resultMessage = message;
        if (crudOperation == CrudOperation.CREATE) {
            resultMessage = "Created " + getServiceName() + ".";
        } else if (crudOperation == CrudOperation.DELETE) {
            resultMessage = "Deleted " + getServiceName() + ".";
        }
        notificationClient.notificate(id, resultMessage);
    }

    protected String compareFields(D anotherDto) {
        D dto = read(anotherDto.getId());
        Collection<String> result = new HashSet<>();
        compareDtos(result, getServiceName(), dto, anotherDto);
        return StringUtils.join(result, ".");
    }

    public D read(UUID uuid) {
        Optional<E> entityById = repository.findById(uuid);
        return entityById.map(mapper::entityToDto).orElse(null);
    }
}
