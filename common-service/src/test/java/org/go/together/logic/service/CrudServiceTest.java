package org.go.together.logic.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.dto.SimpleDto;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.mapper.JoinTestMapper;
import org.go.together.test.mapper.ManyJoinMapper;
import org.go.together.test.mapper.TestMapper;
import org.go.together.test.repository.TestRepository;
import org.go.together.test.service.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.go.together.test.TestUtils.createTestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RepositoryContext.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CrudServiceTest {
    TestDto testDto;
    @Autowired
    private TestService testService;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JoinTestMapper joinTestMapper;
    @Autowired
    private ManyJoinMapper manyJoinMapper;
    @Autowired
    private TestMapper testMapper;

    @BeforeEach
    public void init() {
        UUID id = UUID.randomUUID();
        String name = "test name";
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

        testDto = createTestDto(id, name, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);

        testDto.getManyJoinEntities().stream().map(manyJoinMapper::dtoToEntity).forEach(entityManager::merge);
        testDto.getJoinTestEntities().stream().map(joinTestMapper::dtoToEntity).forEach(entityManager::merge);
    }

    @AfterEach
    public void clean() {
        testDto = null;
        entityManager.clear();
    }

    @Test
    void create() {
        IdDto idDto = testService.create(testDto);

        Optional<TestEntity> savedEntity = testRepository.findById(idDto.getId());

        assertTrue(savedEntity.isPresent());
        assertEquals(testMapper.entityToDto(savedEntity.get()), testDto);
    }

    @Test
    void update() {
        final String newName = "new test name";
        IdDto savedId = testService.create(testDto);
        Optional<TestEntity> savedEntity = testRepository.findById(savedId.getId());
        assertTrue(savedEntity.isPresent());
        testDto.setName(newName);
        IdDto updatedId = testService.update(testDto);
        Optional<TestEntity> updatedEntity = testRepository.findById(updatedId.getId());

        assertTrue(updatedEntity.isPresent());
        assertEquals(savedId, updatedId);
        assertEquals(newName, updatedEntity.get().getName());
    }

    @Test
    void read() {
        IdDto savedId = testService.create(testDto);

        TestDto readDto = testService.read(savedId.getId());

        assertEquals(testDto, readDto);
    }

    @Test
    void delete() {
        IdDto savedId = testService.create(testDto);

        testService.delete(savedId.getId());

        Optional<TestEntity> deletedEntity = testRepository.findById(savedId.getId());

        assertTrue(deletedEntity.isEmpty());
    }

    @Test
    void find() {

    }
}