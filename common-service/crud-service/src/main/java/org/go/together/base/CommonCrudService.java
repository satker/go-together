package org.go.together.base;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.NotificationStatus;
import org.go.together.exceptions.ApplicationException;
import org.go.together.exceptions.ValidationException;
import org.go.together.model.IdentifiedEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public abstract class CommonCrudService<D extends Dto, E extends IdentifiedEntity>
        extends CommonNotificationService<D, E> implements CrudService<D> {
    protected Validator<D> validator;

    @Autowired
    public void setValidator(Validator<D> validator) {
        this.validator = validator;
    }

    @Override
    public IdDto create(UUID requestId, D dto) {
        CrudOperation crudOperation = CrudOperation.CREATE;
        String validationException = validator.validate(requestId, dto, crudOperation);
        if (StringUtils.isBlank(validationException)) {
            UUID id = repository.create().getId();
            E newEntity = mapper.dtoToEntity(dto);
            newEntity.setId(id);
            return dtoAction(requestId, dto, crudOperation, CrudOperation.DELETE, newEntity, dto, newEntity);
        } else {
            throw new ValidationException(requestId, validationException, getServiceName());
        }
    }

    @Override
    public IdDto update(UUID requestId, D updatedDto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        String validationException = validator.validate(requestId, updatedDto, crudOperation);
        if (StringUtils.isBlank(validationException)) {
            E originalEntity = repository.findByIdOrThrow(updatedDto.getId());
            D originalDto = mapper.entityToDto(originalEntity);
            E updatedEntity = mapper.dtoToEntity(updatedDto);
            return dtoAction(requestId, updatedDto, crudOperation, crudOperation, originalEntity, originalDto, updatedEntity);
        } else {

            throw new ValidationException(requestId, validationException, getServiceName());
        }
    }

    @Override
    public D read(UUID requestId, UUID uuid) {
        E entityById = repository.findByIdOrThrow(uuid);
        log.info("Read " + getServiceName() + " row with id: " + uuid.toString());
        return mapper.entityToDto(entityById);
    }

    @Override
    public void delete(UUID requestId, UUID uuid) {
        CrudOperation crudOperation = CrudOperation.DELETE;
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            D originalDto = mapper.entityToDto(entity);
            E enrichedEntity = enrichEntity(requestId, entity, null, crudOperation);
            repository.delete(enrichedEntity);
            sendNotification(uuid, originalDto, originalDto, NotificationStatus.DELETED);
        } else {
            String message = "Cannot find entity " + getServiceName() + "  by id " + uuid;
            log.warn(message);
        }
    }

    private void rollbackAction(UUID requestId, D dto, E entity, Exception e, CrudOperation crudOperation) {
        final String serviceName = getServiceName();
        final String operation = crudOperation.getDescription();
        log.error("Cannot " + operation + StringUtils.SPACE + serviceName + ": " + e.getMessage() +
                ". Try rollback " + serviceName + " with id = " + entity.getId());
        enrichEntity(requestId, entity, dto, crudOperation);
        log.error("Rollback " + operation + " successful for " + serviceName + " with id = " + entity.getId());
    }

    private IdDto dtoAction(UUID requestId,
                            D newDto,
                            CrudOperation crudOperation,
                            CrudOperation revertCrudOperation,
                            E originalEntity,
                            D originalDto,
                            E updatedEntity) {
        E createdEntity;
        try {
            E enrichedEntity = enrichEntity(requestId, updatedEntity, newDto, crudOperation);
            createdEntity = repository.save(enrichedEntity);
        } catch (Exception exception) {
            rollbackAction(requestId, originalDto, originalEntity, exception, revertCrudOperation);
            throw new ApplicationException(exception, requestId);
        }
        sendNotification(newDto.getId(), originalDto, newDto, NotificationStatus.UPDATED);
        return new IdDto(createdEntity.getId());
    }

    protected E enrichEntity(UUID requestId, E entity, D dto, CrudOperation crudOperation) {
        return entity;
    }
}
