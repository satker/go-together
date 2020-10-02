package org.go.together.base.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.CrudService;
import org.go.together.dto.IdDto;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.NotificationStatus;
import org.go.together.exceptions.ApplicationException;
import org.go.together.exceptions.ValidationException;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.dto.form.PageDto;
import org.go.together.find.impl.FindServiceImpl;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.interfaces.NotificationService;
import org.go.together.mapper.Mapper;
import org.go.together.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class CrudServiceImpl<D extends Dto, E extends IdentifiedEntity>
        extends FindServiceImpl<E> implements CrudService<D> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected Mapper<D, E> mapper;
    protected Validator<D> validator;
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected NotificationService<D> notificationService;

    @Autowired
    public void setMapper(Mapper<D, E> mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setValidator(Validator<D> validator) {
        this.validator = validator;
    }

    @Autowired(required = false)
    public void setNotificationService(NotificationService<D> notificationService) {
        this.notificationService = notificationService;
    }

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
            if (dto instanceof ComparableDto) {
                String message = getNotificationMessage(dto, dto, NotificationStatus.CREATED);
                notificationService.createNotification(createdEntity.getId(), dto, message);
            }
            return new IdDto(createdEntity.getId());
        } else {
            log.error("Validation failed " + getServiceName() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    public IdDto update(D dto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        String validate = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            E entityById = repository.findByIdOrThrow(dto.getId());
            D entityToDto = mapper.entityToDto(entityById);
            E mappedEntity = mapper.dtoToEntity(dto);
            E createdEntity;
            try {
                E enrichedEntity = enrichEntity(mappedEntity, dto, crudOperation);
                createdEntity = repository.save(enrichedEntity);
            } catch (Exception e) {
                log.error("Cannot update " + getServiceName() + ": " + e.getMessage());
                log.error("Try rollback update " + getServiceName() + " id = " + mappedEntity.getId());
                enrichEntity(entityById, entityToDto, crudOperation);
                log.error("Rollback successful update " + getServiceName() + " id = " + mappedEntity.getId());
                throw new ApplicationException(e);
            }
            if (dto instanceof ComparableDto) {
                String message = getNotificationMessage(entityToDto, dto, NotificationStatus.UPDATED);
                notificationService.updateNotification(dto.getId(), dto, message);
            }
            return new IdDto(createdEntity.getId());
        } else {
            log.error("Validation failed " + getServiceName() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    public D read(UUID uuid) {
        E entityById = repository.findByIdOrThrow(uuid);
        log.info("Read " + getServiceName() + " row with id: " + uuid.toString());
        return mapper.entityToDto(entityById);
    }

    public void delete(UUID uuid) {
        CrudOperation crudOperation = CrudOperation.DELETE;
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            D dto = mapper.entityToDto(entity);
            E enrichedEntity = enrichEntity(entity, null, crudOperation);
            String message = null;
            if (dto instanceof ComparableDto) {
                D readDto = read(dto.getId());
                message = getNotificationMessage(readDto, readDto, NotificationStatus.DELETED);
            }
            repository.delete(enrichedEntity);
            if (dto instanceof ComparableDto) {
                notificationService.updateNotification(uuid, dto, message);
            }
        } else {
            String message = "Cannot find entity " + getServiceName() + "  by id " + uuid;
            log.warn(message);
        }
    }

    @SneakyThrows
    public ResponseDto<Object> find(FormDto formDto) {
        log.info("Started find in '" + getServiceName() + "' with filter: " +
                objectMapper.writeValueAsString(formDto));
        try {
            Pair<PageDto, Collection<Object>> pageDtoResult = super.findByFormDto(formDto);

            Collection<Object> values = pageDtoResult.getValue();
            if (values != null
                    && !values.isEmpty()
                    && values.iterator().next() instanceof IdentifiedEntity) {
                values = values.stream()
                        .map(value -> (E) value)
                        .map(mapper::entityToDto)
                        .collect(Collectors.toSet());
            }
            log.info("Find in '" + getServiceName() + "' " + Optional.ofNullable(values)
                    .map(Collection::size)
                    .orElse(0) + " rows with filter: " +
                    objectMapper.writeValueAsString(formDto));
            return new ResponseDto<>(pageDtoResult.getKey(), values);
        } catch (Exception exception) {
            throw new ApplicationException(exception);
        }
    }

    public String validate(D dto) {
        return validator.validate(dto, null);
    }

    protected E enrichEntity(E entity, D dto, CrudOperation crudOperation) {
        return entity;
    }

    protected String getNotificationMessage(D dto, D anotherDto, NotificationStatus notificationStatus) {
        String message = notificationService.getMessage(dto, anotherDto, getServiceName(), notificationStatus);
        log.info(message);
        return message;
    }
}
