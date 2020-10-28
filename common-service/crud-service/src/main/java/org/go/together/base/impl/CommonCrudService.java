package org.go.together.base.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.CrudService;
import org.go.together.dto.IdDto;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.NotificationStatus;
import org.go.together.exceptions.ApplicationException;
import org.go.together.exceptions.ValidationException;
import org.go.together.interfaces.Dto;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.validation.Validator;
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
    public IdDto create(D dto) {
        CrudOperation crudOperation = CrudOperation.CREATE;
        String validate = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            UUID id = repository.create().getId();
            E entity = mapper.dtoToEntity(dto);
            entity.setId(id);
            E createdEntity;
            try {
                E enrichedEntity = enrichEntity(entity, dto, crudOperation);
                createdEntity = repository.save(enrichedEntity);
            } catch (Exception e) {
                log.error("Cannot create " + getServiceName() + ": " + e.getMessage());
                log.error("Try rollback create " + getServiceName() + " id = " + entity.getId());
                enrichEntity(entity, dto, CrudOperation.DELETE);
                repository.findById(id).ifPresent(repository::delete);
                log.error("Rollback successful  create " + getServiceName() + " id = " + entity.getId());
                throw new ApplicationException(e);
            }
            sendNotification(createdEntity.getId(), dto, dto, NotificationStatus.UPDATED);
            return new IdDto(createdEntity.getId());
        } else {
            log.error("Validation failed " + getServiceName() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    @Override
    public IdDto update(D updatedDto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        String validate = validator.validate(updatedDto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            E entityById = repository.findByIdOrThrow(updatedDto.getId());
            D originalDto = mapper.entityToDto(entityById);
            E updatedEntity = mapper.dtoToEntity(updatedDto);
            E createdEntity;
            try {
                E enrichedEntity = enrichEntity(updatedEntity, updatedDto, crudOperation);
                createdEntity = repository.save(enrichedEntity);
            } catch (Exception e) {
                log.error("Cannot update " + getServiceName() + ": " + e.getMessage());
                log.error("Try rollback update " + getServiceName() + " id = " + updatedEntity.getId());
                enrichEntity(entityById, originalDto, crudOperation);
                log.error("Rollback successful update " + getServiceName() + " id = " + updatedEntity.getId());
                throw new ApplicationException(e);
            }
            sendNotification(updatedDto.getId(), originalDto, updatedDto, NotificationStatus.UPDATED);
            return new IdDto(createdEntity.getId());
        } else {
            log.error("Validation failed " + getServiceName() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    @Override
    public D read(UUID uuid) {
        E entityById = repository.findByIdOrThrow(uuid);
        log.info("Read " + getServiceName() + " row with id: " + uuid.toString());
        return mapper.entityToDto(entityById);
    }

    @Override
    public void delete(UUID uuid) {
        CrudOperation crudOperation = CrudOperation.DELETE;
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            D originalDto = mapper.entityToDto(entity);
            E enrichedEntity = enrichEntity(entity, null, crudOperation);
            repository.delete(enrichedEntity);
            sendNotification(uuid, originalDto, originalDto, NotificationStatus.DELETED);
        } else {
            String message = "Cannot find entity " + getServiceName() + "  by id " + uuid;
            log.warn(message);
        }
    }

    public String validate(D dto) {
        return validator.validate(dto, null);
    }

    protected E enrichEntity(E entity, D dto, CrudOperation crudOperation) {
        return entity;
    }
}
