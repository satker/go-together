package org.go.together.logic.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.CustomRepository;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationStatus;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.exceptions.ValidationException;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.Mapper;
import org.go.together.logic.Validator;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class CrudService<D extends Dto, E extends IdentifiedEntity> extends NotificationService<D, E> {
    private final Mapper<D, E> mapper;
    private final Validator<D> validator;
    private final CustomRepository<E> repository;

    protected CrudService(CustomRepository<E> repository,
                          Mapper<D, E> mapper,
                          Validator<D> validator) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    public IdDto create(D dto) {
        CrudOperation crudOperation = CrudOperation.CREATE;
        String validate = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            updateEntity(entity, dto, crudOperation);
            E createdEntity = repository.save(entity);
            notificate(createdEntity.getId(), null, NotificationStatus.CREATED);
            addedReceiver(createdEntity.getId(), dto.getAuthorId());
            return new IdDto(createdEntity.getId());
        } else {
            throw new ValidationException(validate);
        }
    }

    public IdDto update(D dto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        String validate = validator.validate(dto, crudOperation);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            updateEntity(entity, dto, crudOperation);
            E createdEntity = repository.save(entity);
            notificate(dto.getId(), dto, NotificationStatus.UPDATED);
            return new IdDto(createdEntity.getId());
        } else {
            throw new ValidationException(validate);
        }
    }

    public void delete(UUID uuid) {
        CrudOperation crudOperation = CrudOperation.DELETE;
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            updateEntity(entity, null, crudOperation);
            repository.delete(entity);
            notificate(uuid, null, NotificationStatus.DELETED);
        } else {
            throw new CannotFindEntityException("Cannot find entity by id " + uuid);
        }
    }

    public ResponseDto<Object> find(FormDto formDto) {
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
        return new ResponseDto<>(pageDtoResult.getKey(), values);
    }

    public String validate(D dto) {
        return validator.validate(dto, null);
    }

    protected void updateEntity(E entity, D dto, CrudOperation crudOperation) {
    }
}
