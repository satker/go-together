package org.go.together.logic;

import org.go.together.dto.IdDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.exceptions.ValidationException;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.Mapper;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.tools.StringUtils;

import java.util.UUID;

public abstract class CrudService<D extends Dto, E extends UpdatableRecordImpl, R extends Repository<E>> {
    private Mapper<D, E> mapper;
    private Validator<D> validator;
    private Repository<E> repository;

    protected CrudService(Repository<E> repository, Mapper<D, E> mapper, Validator<D> validator) {
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
            E createdEntity = repository.create(entity);
            return new IdDto((UUID) createdEntity.get("id"));
        } else {
            throw new ValidationException(validate);
        }
    }

    public IdDto update(D dto) {
        String validate = validator.validateForUpdate(dto);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            E createdEntity = repository.create(entity);
            return new IdDto((UUID) createdEntity.get("id"));
        } else {
            throw new ValidationException(validate);
        }
    }

    public D read(UUID id) {
        E entityById = repository.findById(id);
        return mapper.entityToDto(entityById);
    }

    public void delete(UUID dtoId) {
        E entityById = repository.findById(dtoId);
        if (entityById != null) {
            repository.delete(dtoId);
        }
        throw new CannotFindEntityException("Cannot find entity by id " + dtoId);
    }
}
