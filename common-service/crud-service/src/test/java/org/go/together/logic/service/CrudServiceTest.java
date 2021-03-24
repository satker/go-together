package org.go.together.logic.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.Mapper;
import org.go.together.base.Validator;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.exceptions.ApplicationException;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.repository.interfaces.JoinTestRepository;
import org.go.together.test.repository.interfaces.ManyJoinRepository;
import org.go.together.test.service.interfaces.TestService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.Collectors;

import static org.go.together.enums.FindOperator.*;
import static org.go.together.test.TestUtils.createManyJoinDtos;
import static org.go.together.test.TestUtils.createTestDto;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = RepositoryContext.class)
@TestPropertySource(locations = "/application.properties")
class CrudServiceTest extends CrudServiceCommonTest<TestEntity, TestDto> {
    private static final String MAIN_FIELD_TEST = "test";

    TestDto testDto;

    @Autowired
    private Mapper<JoinTestDto, JoinTestEntity> joinTestMapper;

    @Autowired
    private Mapper<ManyJoinDto, ManyJoinEntity> manyJoinMapper;

    @Autowired
    private ManyJoinRepository manyJoinRepository;

    @Autowired
    private JoinTestRepository joinTestRepository;

    @Autowired
    private Validator<TestDto> validator;

    Random random = new Random();

    @Override
    @BeforeEach
    public void init() {
        super.init();
        TestEntity testEntity = createTestEntity("test name");
        for (int i = 0; i < 5; i++) {
            createTestEntity("test " + 1);
        }
        this.testDto = mapper.entityToDto(testEntity);
    }

    private TestEntity createTestEntity(String name) {
        TestDto testDto = getTestDto(name);

        testDto.getManyJoinEntities().stream().map(manyJoinMapper::dtoToEntity).forEach(manyJoinRepository::save);
        testDto.getJoinTestEntities().stream().map(joinTestMapper::dtoToEntity).forEach(joinTestRepository::save);

        IdDto idDto = crudService.create(testDto);

        return repository.findByIdOrThrow(idDto.getId());
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
        Collection<ManyJoinDto> manyJoinDtos = testDto.getManyJoinEntities();
        manyJoinDtos.addAll(newManyJoinDtos);
        testDto.setManyJoinEntities(new HashSet<>(manyJoinDtos));

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

        testDto.setNumber((long) random.nextInt(10) + 10);
        testDto.setLatitude(3333.3);
        testDto.setEndNumber((long) (random.nextInt(10) + 20));
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
        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "name",
                Set.of(Map.of("name", new FilterValueDto(LIKE, "name"))));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();
        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithOneField() {
        FormDto formDto = getFormDto(MAIN_FIELD_TEST + ".id", "name",
                Set.of(Map.of("name", new FilterValueDto(LIKE, "name"))));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof UUID);
        assertEquals(testDto.getId(), result);
    }

    @Test
    void findByManyToManyTable() {
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "manyJoinEntities.id",
                Set.of(Map.of("id", new FilterValueDto(IN, uuids))));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithUndefinedField() {
        assertThrows(
                ApplicationException.class,
                () -> {
                    Set<UUID> uuids = new HashSet<>();
                    for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
                        uuids.add(manyJoinDto.getId());
                    }
                    FormDto formDto = getFormDto(MAIN_FIELD_TEST, "someUndefinedField",
                            Set.of(Map.of("someUndefinedField", new FilterValueDto(IN, uuids))));
                    findService.find(formDto);
                });
    }

    @Test
    void findFromRemoteServiceToElements() {
        Collection<Object> uuids = new HashSet<>(testDto.getElements());
        ((TestService) crudService).setAnotherClient(uuids);

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "elements?element.id",
                Set.of(Map.of("id", new FilterValueDto(IN, Set.of("filter")))));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToJoinTable() {
        Collection<Object> uuids = new HashSet<>();
        for (JoinTestDto joinTestDto : testDto.getJoinTestEntities()) {
            uuids.add(joinTestDto.getId());
        }
        ((TestService) crudService).setAnotherClient(uuids);

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "joinTestEntities?join.id",
                Set.of(Map.of("id", new FilterValueDto(IN, Set.of("filter")))));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceEmptyResult() {
        ((TestService) crudService).setAnotherClient(Collections.emptyList());

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "joinTestEntities?join.id",
                Set.of(Map.of("id", new FilterValueDto(IN, "filter"))));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(0, objectResponseDto.getResult().size());
        assertEquals(0, objectResponseDto.getPage().getTotalSize());
    }

    @Test
    void findWithGroupOr() {
        Map<String, FilterValueDto> values = Map.of(
                "name", new FilterValueDto(EQUAL,"test name"),
                "number", new FilterValueDto(EQUAL,1)
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "[name|number]", Set.of(values));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithGroupAnd() {
        Map<String, FilterValueDto> values = Map.of(
                "name", new FilterValueDto(EQUAL, "test name"),
                "number", new FilterValueDto(EQUAL, testDto.getNumber())
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "[name&number]", Set.of(values));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroup() {
        Map<String, FilterValueDto> values = Map.of(
                "name", new FilterValueDto(EQUAL, "test name"),
                "number", new FilterValueDto(EQUAL, testDto.getNumber())
        );
        Map<String, FilterValueDto> values1 = Map.of(
                "name", new FilterValueDto(EQUAL, "test"),
                "number", new FilterValueDto(EQUAL, 2)
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "[name&number]", Set.of(values, values1));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroupAndJoinTable() {
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }
        Map<String, FilterValueDto> values = Map.of(
                "name", new FilterValueDto(IN, Set.of("test name")),
                "manyJoinEntities.idw", new FilterValueDto(IN, uuids)
        );
        Map<String, FilterValueDto> values1 = Map.of(
                "name", new FilterValueDto(IN, Set.of(MAIN_FIELD_TEST)),
                "manyJoinEntities.idw", new FilterValueDto(IN, Collections.emptyList())
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "[name&manyJoinEntities.idw]", Set.of(values, values1));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleOuterGroupAndJoinTable() {
        Iterator<JoinTestDto> iterator = testDto.getJoinTestEntities().iterator();
        Map<String, FilterValueDto> values = Map.of(
                "name", new FilterValueDto(IN, Set.of(iterator.next().getComplexInner().getName(),
                        iterator.next().getComplexInner().getName(),
                        iterator.next().getComplexInner().getName()))
        );
        Map<String, FilterValueDto> values1 = Map.of(
                "name", new FilterValueDto(IN, Set.of(MAIN_FIELD_TEST))
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "joinTestEntitiesInner.complexInner.name", Set.of(values, values1));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(3, objectResponseDto.getResult().size());
        assertEquals(6, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
    }

    @Test
    void findWithMultipleOuterGroupAndJoinTableWithId() {
        JoinTestDto joinTestDto = testDto.getJoinTestEntities().iterator().next();
        Map<String, FilterValueDto> values = Map.of(
                "name", new FilterValueDto(EQUAL, joinTestDto.getComplexInner().getName()),
                "id", new FilterValueDto(EQUAL, joinTestDto.getComplexInner().getId())
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "joinTestEntitiesInner.complexInner.[name&id]", Set.of(values));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleOuterGroupAndJoinTableWithDifferentInnerPaths() {
        JoinTestDto joinTestDto = testDto.getJoinTestEntities().iterator().next();
        Map<String, FilterValueDto> values = Map.of(
                "name", new FilterValueDto(EQUAL, joinTestDto.getName()),
                "id", new FilterValueDto(EQUAL, joinTestDto.getComplexInner().getId())
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "joinTestEntitiesInner.[name&complexInner.id]", Set.of(values));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findWithMultipleGroupAndMaskField() {
        Map<String, FilterValueDto> values = Map.of(
                "names", new FilterValueDto(EQUAL, "test name"),
                "numbers", new FilterValueDto(EQUAL, testDto.getNumber())
        );
        Map<String, FilterValueDto> values1 = Map.of(
                "names", new FilterValueDto(EQUAL, MAIN_FIELD_TEST),
                "numbers", new FilterValueDto(EQUAL, 2)
        );

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "[names&numbers]", Set.of(values, values1));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToElementsWithMaskedFields() {
        Collection<Object> uuids = new HashSet<>(testDto.getElements());
        ((TestService) crudService).setAnotherClient(uuids);

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "elementss?element.id",
                Set.of(Map.of("id", new FilterValueDto(IN, Set.of("filter")))));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findFromRemoteServiceToElementsWithGroupFields() {
        Map<String, FilterValueDto> objectObjectHashMap = Map.of(
                "id", new FilterValueDto(IN, Set.of("filter")),
                "name", new FilterValueDto(IN, Set.of("filtername"))
        );
        Collection<Object> uuids = new HashSet<>(testDto.getElements());

        ((TestService) crudService).setAnotherClient(uuids);

        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "elements?element.[id|name]", Set.of(objectObjectHashMap));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findByManyToManyTableWithGroupValues() {
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }
        Map<String, FilterValueDto> map = Map.of(
                "idw", new FilterValueDto(IN, uuids),
                "namew", new FilterValueDto(IN, Set.of("many join test 1"))
        );
        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "manyJoinEntities.[idw&namew]", Set.of(map));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    @Test
    void findByManyToManyTableWithGroupHavingValues() {
        Set<UUID> uuids = new HashSet<>();
        for (ManyJoinDto manyJoinDto : testDto.getManyJoinEntities()) {
            uuids.add(manyJoinDto.getId());
        }
        Map<String, FilterValueDto> map = Map.of(
                "idw", new FilterValueDto(IN, uuids),
                "namew", new FilterValueDto(IN, Set.of("many join test 1"))
        );
        FormDto formDto = getFormDto("test.id:2", "manyJoinEntities.[idw&namew]", Set.of(map));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(0, objectResponseDto.getResult().size());
        assertEquals(0, objectResponseDto.getPage().getTotalSize());
    }

    @Test
    void findByManyToManyTableWithRemoteGroupHavingValues() {
        Map<String, FilterValueDto> objectObjectHashMap = Map.of(
                "id", new FilterValueDto(IN, Set.of("filter")),
                "name", new FilterValueDto(IN, Set.of("filtername"))
        );
        Collection<Object> uuids = new HashSet<>(testDto.getElements());
        ((TestService) crudService).setAnotherClient(uuids);
        FormDto formDto = getFormDto(MAIN_FIELD_TEST, "elements?element.[id|name]:2", Set.of(objectObjectHashMap));
        ResponseDto<Object> objectResponseDto = findService.find(formDto);

        assertEquals(1, objectResponseDto.getResult().size());
        assertEquals(1, objectResponseDto.getPage().getTotalSize());
        Object result = objectResponseDto.getResult().iterator().next();

        assertTrue(result instanceof TestDto);
        assertEquals(testDto, result);
    }

    private FormDto getFormDto(String mainField,
                               String searchField,
                               Collection<Map<String, FilterValueDto>> values) {
        FormDto formDto = new FormDto();
        formDto.setMainIdField(mainField);
        FilterDto filterDto = new FilterDto();
        filterDto.setValues(values);
        formDto.setFilters(Map.of(searchField, filterDto));
        PageDto pageDto = new PageDto();
        pageDto.setPage(0);
        pageDto.setSize(3);
        pageDto.setSort(Collections.emptyList());
        pageDto.setTotalSize(0L);
        formDto.setPage(pageDto);
        return formDto;
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

    private TestDto getTestDto(String name) {
        UUID id = UUID.randomUUID();
        long number = (long) random.nextInt(10) + 10;
        Date date = new Date();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.add(Calendar.MONTH, 1);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startDate);
        endCalendar.add(Calendar.MONTH, 1);
        Date endDate = endCalendar.getTime();
        long startNumber = random.nextInt(9);
        long endNumber = random.nextInt(10) + 20;
        double latitude = 100 * random.nextDouble();
        double longitude = 100 * random.nextDouble();
        SimpleDto simpleDto = new SimpleDto(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        return createTestDto(id, name, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);
    }
}