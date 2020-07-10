package org.go.together.logic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.CustomRepository;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationStatus;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.ApplicationException;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.exceptions.ValidationException;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.Mapper;
import org.go.together.logic.Validator;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class CrudService<D extends ComparableDto, E extends IdentifiedEntity> extends NotificationService<D, E> {
    private final Mapper<D, E> mapper;
    private final Validator<D> validator;
    private final CustomRepository<E> repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected CrudService(CustomRepository<E> repository,
                          Mapper<D, E> mapper,
                          Validator<D> validator) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    public IdDto create(D dto) {
        CrudOperation crudOperation = CrudOperation.CREATE;
        String validate = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            entity.setId(UUID.randomUUID());
            E createdEntity;
            try {
                E enrichedEntity = enrichEntity(entity, dto, crudOperation);
                createdEntity = repository.save(enrichedEntity);
            } catch (Exception e) {
                log.error("Cannot create " + getServiceName() + ": " + e.getMessage());
                log.error("Try rollback create " + getServiceName() + " id = " + entity.getId());
                enrichEntity(entity, dto, CrudOperation.DELETE);
                log.error("Rollback successful  create " + getServiceName() + " id = " + entity.getId());
                throw new ApplicationException(e);
            }
            String message = getNotificationMessage(dto, NotificationStatus.CREATED);
            notificate(createdEntity.getId(), dto, message, NotificationStatus.CREATED);
            return new IdDto(createdEntity.getId());
        } else {
            log.error(dto.getClass().getSimpleName() + " with id " + dto.getId() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    public IdDto update(D dto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        String validate = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            E createdEntity;
            try {
                E enrichedEntity = enrichEntity(entity, dto, crudOperation);
                createdEntity = repository.save(enrichedEntity);
            } catch (Exception e) {
                log.error("Cannot update " + getServiceName() + ": " + e.getMessage());
                E entityById = repository.findById(entity.getId())
                        .orElseThrow(() -> new CannotFindEntityException("Cannot find " + getServiceName() + " by id " +
                                entity.getId()));
                D entityToDto = mapper.entityToDto(entityById);
                log.error("Try rollback update " + getServiceName() + " id = " + entity.getId());
                enrichEntity(entityById, entityToDto, crudOperation);
                log.error("Rollback successful update " + getServiceName() + " id = " + entity.getId());
                throw new ApplicationException(e);
            }
            String message = getNotificationMessage(dto, NotificationStatus.UPDATED);
            notificate(dto.getId(), dto, message, NotificationStatus.UPDATED);
            return new IdDto(createdEntity.getId());
        } else {
            log.error(dto.getClass().getSimpleName() + " with id " + dto.getId() + ": " + validate);
            throw new ValidationException(validate);
        }
    }

    @Override
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
            E enrichedEntity = enrichEntity(entity, null, crudOperation);
            String message = getNotificationMessage(null, NotificationStatus.DELETED);
            repository.delete(enrichedEntity);
            notificate(uuid, null, message, NotificationStatus.DELETED);
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

    protected String getNotificationMessage(D dto, NotificationStatus notificationStatus) {
        return getMessage(dto, notificationStatus);
    }
}
