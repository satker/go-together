package org.go.together.base;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.ApplicationException;
import org.go.together.exceptions.ValidationException;
import org.go.together.model.IdentifiedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public IdDto create(UUID requestId, D dto) {
        CrudOperation crudOperation = CrudOperation.CREATE;
        log.info(requestId + ". Validation for creation " + getServiceName() + " started.");
        String validationException = validator.validate(requestId, dto, crudOperation);
        if (StringUtils.isBlank(validationException)) {
            log.info(requestId + ". Validation for creation " + getServiceName() + " successful.");
            log.info(requestId + ". Creation " + getServiceName() + " started.");
            UUID id = repository.create().getId();
            E newEntity = mapper.dtoToEntity(dto);
            newEntity.setId(id);
            try {
                E enrichedEntity = enrichEntity(requestId, newEntity, dto, crudOperation);
                E createdEntity = repository.save(enrichedEntity);
                log.info(requestId + ". Created " + getServiceName() + " successful.");
                sendNotification(requestId, id, dto, dto, crudOperation);
                return new IdDto(createdEntity.getId());
            } catch (Exception exception) {
                rollbackAction(requestId, dto, newEntity, exception, CrudOperation.DELETE);
                throw new ApplicationException(exception, requestId);
            }
        } else {
            throw new ValidationException(requestId, validationException, getServiceName());
        }
    }

    @Override
    @Transactional
    public IdDto update(UUID requestId, D updatedDto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        log.info(requestId + ". Validation for update " + getServiceName() + " started.");
        String validationException = validator.validate(requestId, updatedDto, crudOperation);
        if (StringUtils.isBlank(validationException)) {
            log.info(requestId + ". Validation for update " + getServiceName() + " with id = '" + updatedDto.getId() + "' successful.");
            log.info(requestId + ". Update " + getServiceName() + " with id = '" + updatedDto.getId() + "' started.");
            E originalEntity = repository.findByIdOrThrow(updatedDto.getId());
            D originalDto = mapper.entityToDto(requestId, originalEntity);
            E updatedEntity = mapper.dtoToEntity(updatedDto);
            try {
                E enrichedEntity = enrichEntity(requestId, updatedEntity, updatedDto, crudOperation);
                E createdEntity = repository.save(enrichedEntity);
                log.info(requestId + ". Updated " + getServiceName() + " successful.");
                sendNotification(requestId, updatedDto.getId(), originalDto, updatedDto, crudOperation);
                return new IdDto(createdEntity.getId());
            } catch (Exception exception) {
                rollbackAction(requestId, originalDto, originalEntity, exception, crudOperation);
                throw new ApplicationException(exception, requestId);
            }
        } else {
            throw new ValidationException(requestId, validationException, getServiceName());
        }
    }

    @Override
    @Transactional
    public D read(UUID requestId, UUID uuid) {
        E entityById = repository.findByIdOrThrow(uuid);
        log.info(requestId + ". Read " + getServiceName() + " row with id: " + uuid.toString());
        return mapper.entityToDto(requestId, entityById);
    }

    @Override
    @Transactional
    public void delete(UUID requestId, UUID uuid) {
        CrudOperation crudOperation = CrudOperation.DELETE;
        if (uuid == null) {
            log.warn(requestId + ". Cannot delete " + getServiceName() + " with null id.");
            return;
        }
        log.info(requestId + ". Deletion " + getServiceName() + " with id = '" + uuid.toString() + "' started.");
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            D originalDto = mapper.entityToDto(requestId, entity);
            E enrichedEntity = enrichEntity(requestId, entity, null, crudOperation);
            repository.delete(enrichedEntity);
            log.info(requestId + ". Deletion " + getServiceName() + " with id = '" + uuid.toString() + "' successful.");
            sendNotification(requestId, uuid, originalDto, originalDto, crudOperation);
        } else {
            log.warn(requestId + ". Cannot find entity " + getServiceName() + "  by id " + uuid);
        }
    }

    @Override
    public boolean checkIfPresent(UUID requestId, UUID uuid) {
        return repository.findById(uuid).isPresent();
    }

    private void rollbackAction(UUID requestId, D dto, E entity, Exception e, CrudOperation crudOperation) {
        final String serviceName = getServiceName();
        final String operation = crudOperation.getDescription();
        log.error(requestId + ". Cannot " + operation + StringUtils.SPACE + serviceName + ": " + e.getMessage() +
                ". Try rollback " + serviceName + " with id = " + entity.getId());
        enrichEntity(requestId, entity, dto, crudOperation);
        log.error(requestId + ". Rollback " + operation + " successful for " + serviceName + " with id = " + entity.getId());
    }

    protected E enrichEntity(UUID requestId, E entity, D dto, CrudOperation crudOperation) {
        return entity;
    }
}
