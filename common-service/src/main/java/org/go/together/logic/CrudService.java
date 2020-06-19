package org.go.together.logic;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.CustomRepository;
import org.go.together.client.NotificationClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.exceptions.ValidationException;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.interfaces.Mapper;
import org.go.together.logic.find.FindService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.go.together.utils.ComparatorUtils.compareDtos;

public abstract class CrudService<D extends Dto, E extends IdentifiedEntity> extends FindService<E> {
    private final Mapper<D, E> mapper;
    private final Validator<D> validator;
    private final CustomRepository<E> repository;

    @Autowired
    private NotificationClient notificationClient;

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
        String validate = validator.validateForCreate(dto);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            updateEntity(entity, dto, crudOperation);
            E createdEntity = repository.save(entity);
            notificate(createdEntity.getId(), null, crudOperation);
            return new IdDto(createdEntity.getId());
        } else {
            throw new ValidationException(validate);
        }
    }

    public IdDto update(D dto) {
        CrudOperation crudOperation = CrudOperation.UPDATE;
        String validate = validator.validateForUpdate(dto);
        if (StringUtils.isBlank(validate)) {
            E entity = mapper.dtoToEntity(dto);
            updateEntity(entity, dto, crudOperation);
            String compareResult = compareFields(dto);
            E createdEntity = repository.save(entity);
            notificate(dto.getId(), compareResult, crudOperation);
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
        CrudOperation crudOperation = CrudOperation.DELETE;
        Optional<E> entityById = repository.findById(uuid);
        if (entityById.isPresent()) {
            E entity = entityById.get();
            updateEntity(entity, null, crudOperation);
            repository.delete(entity);
            notificate(uuid, null, crudOperation);
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

    private void notificate(UUID id, String message, CrudOperation crudOperation) {
        String resultMessage = message;
        if (crudOperation == CrudOperation.CREATE) {
            resultMessage = "Created " + getServiceName() + ".";
        } else if (crudOperation == CrudOperation.DELETE) {
            resultMessage = "Deleted " + getServiceName() + ".";
        }
        notificationClient.notificate(id, resultMessage);
    }

    private String compareFields(D anotherDto) {
        D dto = read(anotherDto.getId());
        Collection<String> result = new HashSet<>();
        compareDtos(result, getServiceName(), dto, anotherDto);
        return StringUtils.join(result, ".");
    }

    public String validate(D dto) {
        return validator.validate(dto);
    }

    protected void updateEntity(E entity, D dto, CrudOperation crudOperation) {
    }
}
