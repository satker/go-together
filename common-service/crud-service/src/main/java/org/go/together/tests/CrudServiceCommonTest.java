package org.go.together.tests;

import org.go.together.base.CrudService;
import org.go.together.dto.IdDto;
import org.go.together.enums.CrudOperation;
import org.go.together.find.dto.*;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.mapper.Mapper;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.exceptions.CannotFindEntityException;
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
    protected CustomRepository<E> repository;

    @Autowired
    protected Mapper<D, E> mapper;

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

        E savedEntity = repository.findById(createdDto.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find saved entity"));


        Map<String, FieldMapper> mappingFields = crudService.getMappingFields();
        if (mappingFields != null) {
            Map<String, FilterDto> findMap = createFilter(savedEntity, mappingFields);
            if (findMap.size() != 0) {
                FormDto formDto = new FormDto();
                formDto.setFilters(findMap);
                formDto.setMainIdField(crudService.getServiceName());
                ResponseDto<Object> objectResponseDto = crudService.find(formDto);

                assertEquals(1, objectResponseDto.getResult().size());
                objectResponseDto.getResult().stream()
                        .map(foundDto -> (D) foundDto)
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
            filterDto.setFilterType(FindSqlOperator.EQUAL);
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
