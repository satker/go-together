package org.go.together.logic;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.exceptions.ValidationException;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.interfaces.Mapper;
import org.go.together.logic.repository.CustomRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class CrudService<D extends Dto, E extends IdentifiedEntity> extends FilterService<E> {
    private final Mapper<D, E> mapper;
    private final Validator<D> validator;
    private final CustomRepository<E> repository;

    protected CrudService(CustomRepository<E> repository, Mapper<D, E> mapper, Validator<D> validator) {
        super(repository);
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    public IdDto create(D dto) {
        String validate = validator.validateForCreate(dto);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            updateEntityForCreate(entity, dto);
            E createdEntity = repository.save(entity);
            return new IdDto(createdEntity.getId());
        } else {
            throw new ValidationException(validate);
        }
    }

    public IdDto update(D dto) {
        String validate = validator.validateForUpdate(dto);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            updateEntityForUpdate(entity, dto);
            E createdEntity = repository.save(entity);
            return new IdDto(createdEntity.getId());
        } else {
            throw new ValidationException(validate);
        }
    }

    public D read(UUID uuid) {
        Optional<E> entityById = repository.findById(uuid);
        return entityById.map(mapper::entityToDto).orElse(null);
    }

    public void delete(UUID uuid) {
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            actionsBeforeDelete(entity);
            repository.delete(entity);
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
        return validator.validate(dto);
    }

    protected void updateEntityForCreate(E entity, D dto) {
    }

    protected void updateEntityForUpdate(E entity, D dto) {
    }

    protected void actionsBeforeDelete(E entity) {
    }
}
