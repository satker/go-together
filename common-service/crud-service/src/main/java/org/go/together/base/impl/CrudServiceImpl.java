package org.go.together.base.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.CrudService;
import org.go.together.dto.IdDto;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.NotificationStatus;
import org.go.together.find.dto.FormDto;
import org.go.together.find.dto.PageDto;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.impl.FindServiceImpl;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.interfaces.NotificationService;
import org.go.together.mapper.Mapper;
import org.go.together.repository.exceptions.ApplicationException;
import org.go.together.repository.exceptions.CannotFindEntityException;
import org.go.together.repository.exceptions.ValidationException;
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
    private Validator<D> validator;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private NotificationService<D> notificationService;

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
                String message = getNotificationMessage(read(dto.getId()), dto, NotificationStatus.CREATED);
                notificationService.notificate(createdEntity.getId(), dto, message, NotificationStatus.CREATED);
            }
            return new IdDto(createdEntity.getId());
        } else {
            log.error(dto.getClass().getSimpleName() + " with id " + dto.getId() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    public IdDto update(D dto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        E entityById = repository.findById(dto.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find " + getServiceName() + " by id " +
                        dto.getId()));
        String validate = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            E createdEntity;
            try {
                E enrichedEntity = enrichEntity(entity, dto, crudOperation);
                createdEntity = repository.save(enrichedEntity);
            } catch (Exception e) {
                log.error("Cannot update " + getServiceName() + ": " + e.getMessage());
                log.error("Try rollback update " + getServiceName() + " id = " + entity.getId());
                D entityToDto = mapper.entityToDto(entityById);
                enrichEntity(entityById, entityToDto, crudOperation);
                log.error("Rollback successful update " + getServiceName() + " id = " + entity.getId());
                throw new ApplicationException(e);
            }
            if (dto instanceof ComparableDto) {
                String message = getNotificationMessage(read(dto.getId()), dto, NotificationStatus.UPDATED);
                notificationService.notificate(dto.getId(), dto, message, NotificationStatus.UPDATED);
            }
            return new IdDto(createdEntity.getId());
        } else {
            log.error(dto.getClass().getSimpleName() + " with id " + dto.getId() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    public D read(UUID uuid) {
        Optional<E> entityById = repository.findById(uuid);
        log.info("Read " + getServiceName() + " " + (entityById.isPresent() ? "1" : "0") + " row with id: " +
                uuid.toString());
        return entityById.map(mapper::entityToDto).orElse(null);
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
                message = getNotificationMessage(read(dto.getId()), dto, NotificationStatus.DELETED);
            }
            repository.delete(enrichedEntity);
            if (dto instanceof ComparableDto) {
                notificationService.notificate(uuid, dto, message, NotificationStatus.DELETED);
            }
        } else {
            String message = getServiceName() + ": " + "Cannot find entity by id " + uuid;
            log.warn(message);
        }
    }

    @SneakyThrows
    public ResponseDto<Object> find(FormDto formDto) {
        log.info("Started find in '" + getServiceName() + "' with filter: " +
                objectMapper.writeValueAsString(formDto));
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
