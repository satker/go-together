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
    public IdDto create(D dto) {
        CrudOperation crudOperation = CrudOperation.CREATE;
        log.info("Validation for creation " + getServiceName() + " started.");
        String validationException = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validationException)) {
            log.info("Validation for creation " + getServiceName() + " successful.");
            log.info("Creation " + getServiceName() + " started.");
            UUID id = repository.create().getId();
            E newEntity = mapper.dtoToEntity(dto);
            newEntity.setId(id);
            try {
                E enrichedEntity = enrichEntity(newEntity, dto, crudOperation);
                E createdEntity = repository.save(enrichedEntity);
                log.info("Created " + getServiceName() + " successful.");
                sendNotification(id, dto, dto, crudOperation);
                return new IdDto(createdEntity.getId());
            } catch (Exception exception) {
                rollbackAction(dto, newEntity, exception, CrudOperation.DELETE);
                throw new ApplicationException(exception);
            }
        } else {
            throw new ValidationException(validationException, getServiceName());
        }
    }

    @Override
    @Transactional
    public IdDto update(D updatedDto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        log.info("Validation for update " + getServiceName() + " started.");
        String validationException = validator.validate(updatedDto, crudOperation);
        if (StringUtils.isBlank(validationException)) {
            log.info("Validation for update " + getServiceName() + " with id = '" + updatedDto.getId() + "' successful.");
            log.info("Update " + getServiceName() + " with id = '" + updatedDto.getId() + "' started.");
            E originalEntity = repository.findByIdOrThrow(updatedDto.getId());
            D originalDto = mapper.entityToDto(originalEntity);
            E updatedEntity = mapper.dtoToEntity(updatedDto);
            try {
                E enrichedEntity = enrichEntity(updatedEntity, updatedDto, crudOperation);
                E createdEntity = repository.save(enrichedEntity);
                log.info("Updated " + getServiceName() + " successful.");
                sendNotification(updatedDto.getId(), originalDto, updatedDto, crudOperation);
                return new IdDto(createdEntity.getId());
            } catch (Exception exception) {
                rollbackAction(originalDto, originalEntity, exception, crudOperation);
                throw new ApplicationException(exception);
            }
        } else {
            throw new ValidationException(validationException, getServiceName());
        }
    }

    @Override
    @Transactional
    public D read(UUID uuid) {
        E entityById = repository.findByIdOrThrow(uuid);
        log.info("Read " + getServiceName() + " row with id: " + uuid.toString());
        return mapper.entityToDto(entityById);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        CrudOperation crudOperation = CrudOperation.DELETE;
        if (uuid == null) {
            log.warn("Cannot delete " + getServiceName() + " with null id.");
            return;
        }
        log.info("Deletion " + getServiceName() + " with id = '" + uuid.toString() + "' started.");
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            D originalDto = mapper.entityToDto(entity);
            E enrichedEntity = enrichEntity(entity, null, crudOperation);
            repository.delete(enrichedEntity);
            log.info("Deletion " + getServiceName() + " with id = '" + uuid.toString() + "' successful.");
            sendNotification(uuid, originalDto, originalDto, crudOperation);
        } else {
            log.warn("Cannot find entity " + getServiceName() + "  by id " + uuid);
        }
    }

    private void rollbackAction(D dto, E entity, Exception e, CrudOperation crudOperation) {
        final String serviceName = getServiceName();
        final String operation = crudOperation.getDescription();
        log.error("Cannot " + operation + StringUtils.SPACE + serviceName + ": " + e.getMessage() +
                ". Try rollback " + serviceName + " with id = " + entity.getId());
        enrichEntity(entity, dto, crudOperation);
        log.error("Rollback " + operation + " successful for " + serviceName + " with id = " + entity.getId());
    }

    protected E enrichEntity(E entity, D dto, CrudOperation crudOperation) {
        return entity;
    }
}
