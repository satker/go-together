package org.go.together.logic.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.dto.SimpleDto;
import org.go.together.find.dto.FilterDto;
import org.go.together.find.dto.FormDto;
import org.go.together.find.dto.PageDto;
import org.go.together.find.dto.ResponseDto;
import org.go.together.repository.exceptions.CannotFindEntityException;
import org.go.together.repository.exceptions.IncorrectFindObject;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.mapper.JoinTestMapper;
import org.go.together.test.mapper.ManyJoinMapper;
import org.go.together.test.repository.JoinTestRepository;
import org.go.together.test.repository.ManyJoinRepository;
import org.go.together.test.service.TestService;
import org.go.together.tests.CrudServiceCommonTest;
import org.go.together.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static org.go.together.find.dto.FindSqlOperator.*;
import static org.go.together.test.TestUtils.createManyJoinDtos;
import static org.go.together.test.TestUtils.createTestDto;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = RepositoryContext.class)
class CrudServiceTest extends CrudServiceCommonTest<TestEntity, TestDto> {
    TestDto testDto;

    @Autowired
    private JoinTestMapper joinTestMapper;

    @Autowired
    private ManyJoinMapper manyJoinMapper;

    @Autowired
    private ManyJoinRepository manyJoinRepository;

    @Autowired
    private JoinTestRepository joinTestRepository;

    @Autowired
    private Validator<TestDto> validator;

    @BeforeEach
    public void init() {
        super.init();
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

        TestDto testDto = createTestDto(id, name, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);

        testDto.getManyJoinEntities().stream().map(manyJoinMapper::dtoToEntity).forEach(manyJoinRepository::save);
        testDto.getJoinTestEntities().stream().map(joinTestMapper::dtoToEntity).forEach(joinTestRepository::save);

        IdDto idDto = crudService.create(testDto);

        TestEntity testEntity = repository.findById(idDto.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find TestDto"));
        this.testDto = mapper.entityToDto(testEntity);
    }

    @AfterEach
    public void clean() {
        testDto = null;
        repository.findAll().forEach(repository::delete);
        joinTestRepository.findAll().forEach(joinTestRepository::delete);
        manyJoinRepository.findAll().forEach(manyJoinRepository::delete);
        ((TestService) crudService).setAnotherClient(null);
    }

    @Test
    void create() {
        Optional<TestEntity> savedEntity = repository.findById(testDto.getId());

        assertTrue(savedEntity.isPresent());
        assertEquals(mapper.entityToDto(savedEntity.get()), testDto);
    }

    @Test
    void update() {
        final String newName = "new test name";
        Optional<TestEntity> savedEntity = repository.findById(testDto.getId());
        assertTrue(savedEntity.isPresent());
        testDto.setName(newName);
        IdDto updatedId = crudService.update(testDto);
        Optional<TestEntity> updatedEntity = repository.findById(updatedId.getId());

        assertTrue(updatedEntity.isPresent());
        assertEquals(testDto.getId(), updatedId.getId());
        assertEquals(newName, updatedEntity.get().getName());
    }

    @Test
    void updateInnerDtos() {
        final String newName = "new test name";
        Optional<TestEntity> savedEntity = repository.findById(testDto.getId());
        assertTrue(savedEntity.isPresent());
        testDto.setName(newName);

        Set<ManyJoinDto> newManyJoinDtos = createManyJoinDtos().stream()
                .skip(2)
                .limit(3)
                .collect(Collectors.toSet());
        Set<ManyJoinDto> manyJoinDtos = testDto.getManyJoinEntities();
        manyJoinDtos.addAll(newManyJoinDtos);
        testDto.setManyJoinEntities(manyJoinDtos);

        testDto.setJoinTestEntities(testDto.getJoinTestEntities().stream()
                .skip(4)
                .limit(4)
                .collect(Collectors.toSet()));

        IdDto updatedId = crudService.update(testDto);
        Optional<TestEntity> updatedEntity = repository.findById(updatedId.getId());

        assertTrue(updatedEntity.isPresent());
        assertEquals(testDto.getId(), updatedId.getId());
        assertEquals(newName, updatedEntity.get().getName());
    }

    @Test
    void updateMultipleFields() {
        final String newName = "new test name";
        Optional<TestEntity> savedEntity = repository.findById(testDto.getId());
        assertTrue(savedEntity.isPresent());
        testDto.setName(newName);
        testDto.setSimpleDto(new SimpleDto("newId", "new name"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(testDto.getDate());
        calendar.add(Calendar.MONTH, 1);
        Date date = calendar.getTime();
        testDto.setDate(date);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(testDto.getEndDate());
        endCalendar.add(Calendar.MONTH, 1);
        Date endDate = endCalendar.getTime();
        testDto.setEndDate(endDate);

        testDto.setNumber(2);
        testDto.setLatitude(3333.3);
        testDto.setEndNumber(4);
        IdDto updatedId = crudService.update(testDto);
        Optional<TestEntity> updatedEntity = repository.findById(updatedId.getId());

        assertTrue(updatedEntity.isPresent());
        assertEquals(testDto.getId(), updatedId.getId());
        assertEquals(newName, updatedEntity.get().getName());
    }

    @Test
    void read() {
        TestDto readDto = crudService.read(testDto.getId());

        assertEquals(testDto, readDto);
    }

    @Test
    void delete() {
        crudService.delete(testDto.getId());

        Optional<TestEntity> deletedEntity = repository.findById(testDto.getId());

        assertTrue(deletedEntity.isEmpty());
    }

    @Test
    void validate() {
        String validate = validator.validate(testDto, null);

        assertTrue(StringUtils.isBlank(validate));
    }

    @Test
    void find() {
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
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithOneField() {
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
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof UUID);
        assertEquals(testDto.getId(), result);
    }

    @Test
    void findByManyToManyTable() {
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
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithUndefinedField() {
        assertThrows(
                IncorrectFindObject.class,
                () -> {
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
                    crudService.find(formDto);
                });
    }

    @Test
    void findFromRemoteServiceToElements() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", Collections.singleton("filter"))));
        Collection<Object> uuids = new HashSet<>(testDto.getElements());
        ((TestService) crudService).setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("elements?element.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToJoinTable() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", Collections.singleton("filter"))));
        Collection<Object> uuids = new HashSet<>();
        for (JoinTestDto joinTestDto : testDto.getJoinTestEntities()) {
            uuids.add(joinTestDto.getId());
        }
        ((TestService) crudService).setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("joinTestEntities.id?join.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceEmptyResult() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", "filter")));
        ((TestService) crudService).setAnotherClient(Collections.emptyList());
        formDto.setFilters(Collections.singletonMap("joinTestEntities.id?join.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(0, objectResponseDto.getResult().size());
        assertEquals(0, objectResponseDto.getPage().getTotalSize());
    }

    @Test
    void findWithGroup() {
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
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroup() {
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
        filterDto.setValues(Set.of(values, values1));
        formDto.setFilters(Collections.singletonMap("[name&number]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroupAndJoinTable() {
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
        values.put("manyJoinEntities.idw", uuids);
        Map<String, Object> values1 = new HashMap<>();
        values1.put("name", Collections.singleton("test"));
        values1.put("manyJoinEntities.idw", Collections.emptyList());
        filterDto.setValues(Set.of(values, values1));
        formDto.setFilters(Collections.singletonMap("[name&manyJoinEntities.idw]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroupAndMaskField() {
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
        filterDto.setValues(Set.of(values, values1));
        formDto.setFilters(Collections.singletonMap("[names&numbers]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToElementsWithMaskedFields() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", Collections.singleton("filter"))));
        Collection<Object> uuids = new HashSet<>(testDto.getElements());
        ((TestService) crudService).setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("elementss?element.id", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToElementsWithGroupFields() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("id", Collections.singleton("filter"));
        objectObjectHashMap.put("name", Collections.singleton("filtername"));
        filterDto.setValues(Collections.singleton(objectObjectHashMap));
        Collection<Object> uuids = new HashSet<>(testDto.getElements());
        ((TestService) crudService).setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("elements?element.[id|name]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findByManyToManyTableWithGroupValues() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("idw", uuids);
        map.put("namew", Collections.singleton("many join test 1"));
        filterDto.setValues(Collections.singleton(map));
        formDto.setFilters(Collections.singletonMap("manyJoinEntities.[idw&namew]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findByManyToManyTableWithGroupHavingValues() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test.id:2");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("idw", uuids);
        map.put("namew", Collections.singleton("many join test 1"));
        filterDto.setValues(Collections.singleton(map));
        formDto.setFilters(Collections.singletonMap("manyJoinEntities.[idw&namew]", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(0, objectResponseDto.getResult().size());
        assertEquals(0, objectResponseDto.getPage().getTotalSize());
    }

    @Test
    void findByManyToManyTableWithRemoteGroupHavingValues() {
        FormDto formDto = new FormDto();
        formDto.setMainIdField("test");
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(IN);
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("id", Collections.singleton("filter"));
        objectObjectHashMap.put("name", Collections.singleton("filtername"));
        filterDto.setValues(Collections.singleton(objectObjectHashMap));
        Collection<Object> uuids = new HashSet<>(testDto.getElements());
        ((TestService) crudService).setAnotherClient(uuids);
        formDto.setFilters(Collections.singletonMap("elements?element.[id|name]:2", filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        ResponseDto<Object> objectResponseDto = crudService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Override
    protected TestDto createDto() {
        TestDto testDto = factory.manufacturePojo(TestDto.class);
        testDto.setName("test_" + testDto.getName());
        testDto.setDate(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(testDto.getDate());
        calendar.add(Calendar.MONTH, rand.nextInt(10) + 1);
        testDto.setStartDate(calendar.getTime());

        calendar.add(Calendar.MONTH, rand.nextInt(10) + 1);
        testDto.setEndDate(calendar.getTime());

        testDto.setStartNumber(generateLong(1, Long.MAX_VALUE));
        testDto.setEndNumber(generateLong(testDto.getStartNumber() + 1, Long.MAX_VALUE));

        testDto.setNumber(generateLong(testDto.getStartNumber(), testDto.getEndNumber()));

        String string = factory.manufacturePojo(String.class);
        testDto.setSimpleDto(new SimpleDto(string, string));

        return testDto;
    }
}