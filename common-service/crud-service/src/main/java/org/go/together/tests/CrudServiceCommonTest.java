package org.go.together.tests;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.*;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.FindOperator;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.interfaces.Identified;
import org.go.together.model.IdentifiedEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class CrudServiceCommonTest<E extends IdentifiedEntity, D extends Dto> {
    protected PodamFactory factory = new PodamFactoryImpl();
    protected Random rand = new Random();

    @Autowired
    protected CrudService<D> crudService;

    @Autowired
    protected FindService<D> findService;

    @Autowired
    protected CustomRepository<E> repository;

    @Autowired
    protected Mapper<D, E> mapper;

    @Autowired
    protected Validator<D> validator;

    protected D dto;
    protected D updatedDto;

    @BeforeEach
    public void init() {
        dto = createDto();
        updatedDto = createDto();
    }

    @AfterEach
    public void clean() {
        repository.findAll().forEach(repository::delete);
    }

    @Test
    public void validateDto() {
        assertTrue(StringUtils.isBlank(validator.validate(dto, null)));
    }

    @Test
    public void createTest() {
        D createdDto = getCreatedEntityId(dto);
        D savedObject = crudService.read(createdDto.getId());
        checkDtos(dto, savedObject, CrudOperation.CREATE);
    }

    @Test
    public void updateTest() {
        D createdDto = getCreatedEntityId(dto);
        updatedDto.setId(createdDto.getId());
        IdDto update = crudService.update(updatedDto);
        D savedUpdatedObject = crudService.read(update.getId());
        checkDtos(updatedDto, savedUpdatedObject, CrudOperation.UPDATE);
    }

    @Test
    public void updateTestWithNotPresentedId() {
        assertThrows(CannotFindEntityException.class, () -> crudService.update(dto));
    }

    @Test
    public void deleteTest() {
        D createdDto = getCreatedEntityId(dto);
        Optional<E> entityById = repository.findById(createdDto.getId());
        assertTrue(entityById.isPresent());
        crudService.delete(createdDto.getId());
        Optional<E> deletedEntityById = repository.findById(createdDto.getId());
        assertTrue(deletedEntityById.isEmpty());
    }

    @Test
    public void deleteTestIfNotPresent() {
        UUID deletedUUID = UUID.randomUUID();
        Optional<E> deletedEntityById = repository.findById(deletedUUID);
        assertTrue(deletedEntityById.isEmpty());
        crudService.delete(deletedUUID);
        deletedEntityById = repository.findById(deletedUUID);
        assertTrue(deletedEntityById.isEmpty());
    }

    @Test
    public void findTest() {
        D createdDto = getCreatedEntityId(dto);

        E savedEntity = repository.findByIdOrThrow(createdDto.getId());


        Map<String, FieldMapper> mappingFields = findService.getMappingFields();
        if (mappingFields != null) {
            Map<String, FilterDto> findMap = createFilter(savedEntity, mappingFields);
            if (findMap.size() != 0) {
                FormDto formDto = new FormDto();
                formDto.setFilters(findMap);
                formDto.setMainIdField(findService.getServiceName());
                ResponseDto<Object> objectResponseDto = findService.find(formDto);

                assertEquals(1, objectResponseDto.getResult().size());
                objectResponseDto.getResult().stream()
                        .map(Identified::<D>cast)
                        .forEach(foundDto -> checkDtos(foundDto, dto, CrudOperation.CREATE));
            }
        }
    }

    protected D getCreatedEntityId(D dto) {
        UUID id = crudService.create(dto).getId();
        dto.setId(id);
        return dto;
    }

    private Map<String, FilterDto> createFilter(E savedEntity, Map<String, FieldMapper> mappingFields) {
        return mappingFields.entrySet().stream()
                .filter(entry -> entry.getValue().getRemoteServiceClient() == null)
                .filter(entry -> entry.getValue().getInnerService() == null)
                .filter(entry -> entry.getValue().getCurrentServiceField() != null)
                .map(entry -> fillFilter(savedEntity, entry))
                .filter(Objects::nonNull)
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, FilterDto> fillFilter(E savedEntity, Map.Entry<String, FieldMapper> entry) {
        String currentServiceField = entry.getValue().getCurrentServiceField();
        try {
            Field declaredField = savedEntity.getClass().getDeclaredField(currentServiceField);
            declaredField.setAccessible(true);
            Object value = declaredField.get(savedEntity);
            FilterDto filterDto = new FilterDto();
            filterDto.setFilterType(FindOperator.EQUAL);
            filterDto.setValues(Collections.singleton(Map.of(entry.getKey(), value)));
            return Map.entry(entry.getKey(), filterDto);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void checkDtos(D dto, D savedObject, CrudOperation operation) {
        assertEquals(dto, savedObject);
    }

    protected abstract D createDto();

    protected long generateLong(long min, long max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
