package org.go.together.logic;

import org.go.together.dto.IdDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.exceptions.ValidationException;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.interfaces.Mapper;
import org.go.together.logic.repository.CustomRepository;
import org.jooq.tools.StringUtils;

import java.util.Optional;
import java.util.UUID;

public abstract class CrudService<D extends Dto, E extends IdentifiedEntity> {
    private Mapper<D, E> mapper;
    private Validator<D> validator;
    private CustomRepository<E> repository;

    protected CrudService(CustomRepository<E> repository, Mapper<D, E> mapper, Validator<D> validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }


    public void setViewMapper(Mapper<D, E> mapper) {
        this.mapper = mapper;
    }

    public void setValidator(Validator<D> validator) {
        this.validator = validator;
    }

    public IdDto create(D dto) {
        String validate = validator.validateForCreate(dto);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
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
        entityById.ifPresent(e -> repository.delete(e));
        throw new CannotFindEntityException("Cannot find entity by id " + uuid);
    }
}
