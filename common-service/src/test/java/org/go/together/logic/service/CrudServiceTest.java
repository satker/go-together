package org.go.together.logic.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
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
import java.util.*;

import static org.go.together.logic.find.enums.FindSqlOperator.*;
import static org.go.together.test.TestUtils.createTestDto;
import static org.junit.jupiter.api.Assertions.*;

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
        testService.setAnotherClient(null);
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
    void validate() {
        String validate = testService.validate(testDto);

        assertTrue(StringUtils.isBlank(validate));
    }

    @Test
    void find() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(LIKE);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("name", "test")));
        formDto.setFilters(Collections.singletonMap("name", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithOneField() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test.id");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(LIKE);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("name", "test")));
        formDto.setFilters(Collections.singletonMap("name", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof UUID);
        assertEquals(testDto.getId(), result);
    }

    @Test
    void findByManyToManyTable() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", uuids)));
        formDto.setFilters(Collections.singletonMap("manyJoinEntities.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithUndefinedField() {
        assertThrows(
                IncorrectFindObject.class,
                () -> {
                    testService.create(testDto);

                    FormDto formDto = new FormDto();
                    formDto.setMainIdField("test");
                    FilterDto filterDto = new FilterDto();
                    filterDto.setFilterType(IN);
                    Set<UUID> uuids = new HashSet<>();
                    for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
                        uuids.add(manyJoinDto.getId());
                    }
                    filterDto.setValues(Collections.singleton(Collections.singletonMap("someUndefinedField", uuids)));
                    formDto.setFilters(Collections.singletonMap("someUndefinedField", filterDto));
                    PageDto pageDto = new PageDto();
                    pageDto.setPage(0);
                    pageDto.setSize(3);
                    pageDto.setSort(Collections.emptyList());
                    pageDto.setTotalSize(0L);
                    formDto.setPage(pageDto);
                    testService.find(formDto);
                });
    }

    @Test
    void findFromRemoteServiceToElements() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", Collections.singleton("filter"))));
        Collection<Object> uuids = new HashSet<>();
        for (String uuid : testDto.getElements()) {
            uuids.add(UUID.fromString(uuid));
        }
        testService.setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("elements?element.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToJoinTable() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", Collections.singleton("filter"))));
        Collection<Object> uuids = new HashSet<>();
        for (JoinTestDto joinTestDto : testDto.getJoinTestEntities()) {
            uuids.add(joinTestDto.getId());
        }
        testService.setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("joinTestEntities.id?join.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceEmptyResult() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", "filter")));
        testService.setAnotherClient(Collections.emptyList());
        formDto.setFilters(Collections.singletonMap("joinTestEntities.id?join.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        assertEquals(0, objectResponseDto.getResult().size());
        assertEquals(0, objectResponseDto.getPage().getTotalSize());
    }

    @Test
    void findWithGroup() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(EQUAL);
        Map<String, Object> values = new HashMap<>();
        values.put("name", "test name");
        values.put("number", 1);
        filterDto.setValues(Collections.singleton(values));
        formDto.setFilters(Collections.singletonMap("[name&number]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroup() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(EQUAL);
        Map<String, Object> values = new HashMap<>();
        values.put("name", "test name");
        values.put("number", 1);
        Map<String, Object> values1 = new HashMap<>();
        values1.put("name", "test");
        values1.put("number", 2);
        filterDto.setValues(Arrays.asList(values, values1));
        formDto.setFilters(Collections.singletonMap("[name&number]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroupAndJoinTable() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        Map<String, Object> values = new HashMap<>();
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }
        values.put("name", Collections.singleton("test name"));
        values.put("id", uuids);
        Map<String, Object> values1 = new HashMap<>();
        values1.put("name", Collections.singleton("test"));
        values1.put("id", Collections.emptyList());
        filterDto.setValues(Arrays.asList(values, values1));
        formDto.setFilters(Collections.singletonMap("[name&manyJoinEntities.id]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroupAndMaskField() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(EQUAL);
        Map<String, Object> values = new HashMap<>();
        values.put("names", "test name");
        values.put("numbers", 1);
        Map<String, Object> values1 = new HashMap<>();
        values1.put("names", "test");
        values1.put("numbers", 2);
        filterDto.setValues(Arrays.asList(values, values1));
        formDto.setFilters(Collections.singletonMap("[names&numbers]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToElementsWithMaskedFields() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", Collections.singleton("filter"))));
        Collection<Object> uuids = new HashSet<>();
        for (String uuid : testDto.getElements()) {
            uuids.add(UUID.fromString(uuid));
        }
        testService.setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("elementss?element.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToElementsWithGroupFields() {
        testService.create(testDto);

        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("id", Collections.singleton("filter"));
        objectObjectHashMap.put("name", Collections.singleton("filtername"));
        filterDto.setValues(Collections.singleton(objectObjectHashMap));
        Collection<Object> uuids = new HashSet<>();
        for (String uuid : testDto.getElements()) {
            uuids.add(UUID.fromString(uuid));
        }
        testService.setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("elements?element.[id|name]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = testService.find(formDto);

        Object result = objectResponseDto.getResult().iterator().next();

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }
}