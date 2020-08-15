package org.go.together.logic.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.SimpleDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.ApplicationException;
import org.go.together.exceptions.ValidationException;
import org.go.together.find.dto.FieldMapper;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.mapper.JoinTestMapper;
import org.go.together.test.mapper.ManyJoinMapper;
import org.go.together.test.mapper.TestMapper;
import org.go.together.test.repository.JoinTestRepository;
import org.go.together.test.repository.ManyJoinRepository;
import org.go.together.test.repository.TestRepository;
import org.go.together.test.validation.TestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.go.together.test.TestUtils.createTestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryContext.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CrudServiceRevertTest {
    private static final String TEST_NAME = "test name";
    private static final String CREATED_TEST_NAME = "create test name";

    @Autowired
    private JoinTestMapper joinTestMapper;

    @Autowired
    private ManyJoinMapper manyJoinMapper;

    @Autowired
    private ManyJoinRepository manyJoinRepository;

    @Autowired
    private JoinTestRepository joinTestRepository;

    @Mock
    private TestRepository testRepository;

    @Autowired
    private TestValidator testValidator;

    @Autowired
    private TestMapper testMapper;

    private CrudServiceImpl<TestDto, TestEntity> testServiceOverride;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        testServiceOverride = new CrudServiceImpl<>() {
            @Override
            protected TestEntity enrichEntity(TestEntity entity, TestDto dto, CrudOperation crudOperation) {
                if (crudOperation == CrudOperation.CREATE) {
                    assertEquals(TEST_NAME, entity.getName());
                    entity.setName(CREATED_TEST_NAME);
                } else if (crudOperation == CrudOperation.DELETE) {
                    assertEquals(CREATED_TEST_NAME, entity.getName());
                }
                return entity;
            }

            @Override
            public String getServiceName() {
                return "testServiceOverride";
            }

            @Override
            public Map<String, FieldMapper> getMappingFields() {
                return null;
            }
        };
    }

    private TestDto getTestDto() {
        UUID id = UUID.randomUUID();
        long number = 1;
        Date date = new Date();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.add(Calendar.MONTH, 1);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startDate);
        endCalendar.add(Calendar.MONTH, 1);
        Date endDate = endCalendar.getTime();
        long startNumber = 1;
        long endNumber = 3;
        double latitude = 18.313230192867607;
        double longitude = 74.39449363632201;
        SimpleDto simpleDto = new SimpleDto("simpleDto", "simpleDto");

        TestDto testDto = createTestDto(id, TEST_NAME, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);

        testDto.getManyJoinEntities().stream().map(manyJoinMapper::dtoToEntity).forEach(manyJoinRepository::save);
        testDto.getJoinTestEntities().stream().map(joinTestMapper::dtoToEntity).forEach(joinTestRepository::save);
        return testDto;
    }

    @Test
    public void revertCreate() {
        assertThrows(ApplicationException.class, () -> {
            TestEntity testEntity = new TestEntity();
            testEntity.setId(UUID.randomUUID());
            when(testRepository.create()).thenReturn(testEntity);
            when(testRepository.findById(testEntity.getId())).thenReturn(Optional.of(testEntity));
            when(testRepository.save(any())).thenThrow(RuntimeException.class);

            testServiceOverride.setMapper(testMapper);
            testServiceOverride.setValidator(testValidator);
            testServiceOverride.setRepository(testRepository);

            testServiceOverride.create(getTestDto());
        });
    }

    @Test
    public void revertUpdate() {
        assertThrows(ApplicationException.class, () -> {
            TestDto testDto = getTestDto();
            TestEntity testEntity = testMapper.dtoToEntity(testDto);

            when(testRepository.findByIdOrThrow(testDto.getId())).thenReturn(testEntity);
            when(testRepository.save(any())).thenThrow(RuntimeException.class);
            when(testRepository.findByIdOrThrow(testDto.getId())).thenReturn(testEntity);
            testServiceOverride.setMapper(testMapper);
            testServiceOverride.setValidator(testValidator);
            testServiceOverride.setRepository(testRepository);

            testServiceOverride.update(testDto);
        });
    }

    @Test
    public void revertCreateWithNotValid() {
        assertThrows(ValidationException.class, () -> {
            testServiceOverride.setValidator(testValidator);
            testServiceOverride.setRepository(testRepository);
            testServiceOverride.create(null);
        });
    }

    @Test
    public void revertUpdateWithNotValid() {
        assertThrows(ValidationException.class, () -> {
            testServiceOverride.setValidator(testValidator);
            testServiceOverride.setRepository(testRepository);
            testServiceOverride.update(null);
        });
    }
}
