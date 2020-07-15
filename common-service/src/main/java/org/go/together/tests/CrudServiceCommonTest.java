package org.go.together.tests;

import org.go.together.CustomRepository;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FindSqlOperator;
import org.go.together.dto.filter.FormDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.Mapper;
import org.go.together.logic.services.CrudService;
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
public abstract class CrudServiceCommonTest<E extends IdentifiedEntity, D extends ComparableDto> {
    protected PodamFactory factory = new PodamFactoryImpl();
    protected Random rand = new Random();

    @Autowired
    protected CrudService<D, E> crudService;

    @Autowired
    protected CustomRepository<E> repository;

    @Autowired
    protected Mapper<D, E> mapper;

    @Test
    public void createTest() {
        D dto = createDto();
        IdDto idDto = crudService.create(dto);
        dto.setId(idDto.getId());
        D savedObject = crudService.read(idDto.getId());
        checkDtos(dto, savedObject, CrudOperation.CREATE);
    }

    @Test
    public void updateTest() {
        D dto = createDto();
        IdDto idDto = crudService.create(dto);
        D updatedObject = createDto();
        updatedObject.setId(idDto.getId());
        IdDto update = crudService.update(updatedObject);
        D savedUpdatedObject = crudService.read(update.getId());
        checkDtos(updatedObject, savedUpdatedObject, CrudOperation.UPDATE);
    }

    @Test
    public void updateTestWithNotPresentedId() {
        assertThrows(CannotFindEntityException.class, () -> crudService.update(createDto()));
    }

    @Test
    public void deleteTest() {
        D dto = createDto();
        IdDto idDto = crudService.create(dto);
        Optional<E> entityById = repository.findById(idDto.getId());
        assertTrue(entityById.isPresent());
        crudService.delete(idDto.getId());
        Optional<E> deletedEntityById = repository.findById(idDto.getId());
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
        D dto = createDto();
        IdDto idDto = crudService.create(dto);
        dto.setId(idDto.getId());

        E savedEntity = repository.findById(idDto.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find saved entity"));


        Map<String, FieldMapper> mappingFields = crudService.getMappingFields();
        if (mappingFields != null) {
            Map<String, FilterDto> findMap = mappingFields.entrySet().stream()
                    .filter(entry -> entry.getValue().getRemoteServiceClient() == null)
                    .filter(entry -> entry.getValue().getInnerService() == null)
                    .filter(entry -> entry.getValue().getCurrentServiceField() != null)
                    .map(entry -> {
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
                    })
                    .filter(Objects::nonNull)
                    .filter(entry -> entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (findMap.size() != 0) {
                FormDto formDto = new FormDto();
                formDto.setFilters(findMap);
                formDto.setMainIdField(crudService.getServiceName());
                ResponseDto<Object> objectResponseDto = crudService.find(formDto);

                objectResponseDto.getResult().stream()
                        .map(foundDto -> (D) foundDto)
                        .forEach(foundDto -> checkDtos(foundDto, dto, CrudOperation.CREATE));
            }
        }
    }

    protected void checkDtos(D dto, D savedObject, CrudOperation operation) {
        assertEquals(dto, savedObject);
    }

    protected abstract D createDto();

    protected long generateLong(long min, long max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
