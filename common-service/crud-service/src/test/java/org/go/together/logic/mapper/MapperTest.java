package org.go.together.logic.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.SimpleDto;
import org.go.together.interfaces.Identified;
import org.go.together.mapper.Mapper;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.entities.TestEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryContext.class)
class MapperTest {
    @Autowired
    private Mapper<TestDto, TestEntity> testMapper;

    TestDto testDto;
    TestEntity testEntity;

    @BeforeEach
    void init() {
        UUID id = UUID.randomUUID();
        String name = "test name";
        long number = 6;
        Date date = new Date();
        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.MONTH, 1);
        Date endDate = c.getTime();
        long startNumber = 1;
        long endNumber = 3;
        double latitude = 18.313230192867607;
        double longitude = 74.39449363632201;
        SimpleDto simpleDto = new SimpleDto("simpleDto", "simpleDto");

        Pair<TestEntity, TestDto> testDtoAndEntity = createTestDtoAndEntity(id, name, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);

        testDto = testDtoAndEntity.getRight();
        testEntity = testDtoAndEntity.getLeft();
    }

    @AfterEach
    void clean() {
        testDto = null;
        testEntity = null;
    }

    @Test
    void entityToDto() {
        TestDto actualTestDto = testMapper.entityToDto(testEntity);

        assertEquals(testDto, actualTestDto);
    }

    @Test
    void dtoToEntity() {
        TestEntity actualTestEntity = testMapper.dtoToEntity(testDto);

        assertEquals(testEntity, actualTestEntity);
    }

    @Test
    void entitiesToDtos() {
        UUID id = UUID.randomUUID();
        String name = "test name1";
        long number = 62;
        Date date = new Date();
        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.MONTH, 3);
        Date endDate = c.getTime();
        long startNumber = 11;
        long endNumber = 32;
        SimpleDto simpleDto = new SimpleDto("simpleDtoNext", "simpleDtoNext");
        double longitude = 74.48513084335326;
        double latitude = 19.0625353790555;

        Pair<TestEntity, TestDto> testDtoAndEntity = createTestDtoAndEntity(id, name, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);

        Collection<TestDto> testDtos = Set.of(testDto, testDtoAndEntity.getRight());
        Collection<TestEntity> testEntities = Set.of(testEntity, testDtoAndEntity.getLeft());

        Collection<TestDto> actualTestDtos = testMapper.entitiesToDtos(testEntities);

        assertEquals(2, actualTestDtos.size());
        assertTrue(compareCollections(testDtos, actualTestDtos));
    }

    @Test
    void dtosToEntities() {
        UUID id = UUID.randomUUID();
        String name = "test name2";
        long number = 65;
        Date date = new Date();
        Date startDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.MONTH, 5);
        Date endDate = c.getTime();
        long startNumber = 112;
        long endNumber = 323;
        SimpleDto simpleDto = new SimpleDto("simpleDtoNext1", "simpleDtoNext1");
        double longitude = 73.12419944686889;
        double latitude = 18.796236122618033;

        Pair<TestEntity, TestDto> testDtoAndEntity = createTestDtoAndEntity(id, name, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);

        Collection<TestDto> testDtos = Set.of(testDto, testDtoAndEntity.getRight());
        Collection<TestEntity> testEntities = Set.of(testEntity, testDtoAndEntity.getLeft());

        Collection<TestEntity> actualTestEntites = testMapper.dtosToEntities(testDtos);

        assertEquals(2, actualTestEntites.size());
        assertTrue(compareCollections(testEntities, actualTestEntites));
    }

    private boolean compareCollections(Collection<? extends Identified> oneCollection,
                                       Collection<? extends Identified> anotherCollection) {
        Map<UUID, Set<Identified>> oneMap = oneCollection.stream()
                .collect(Collectors.groupingBy(Identified::getId, Collectors.toSet()));
        Map<UUID, Set<Identified>> anotherMap = anotherCollection.stream()
                .collect(Collectors.groupingBy(Identified::getId, Collectors.toSet()));

        return oneMap.keySet().stream()
                .allMatch(key -> oneMap.get(key).equals(anotherMap.get(key)));
    }


    private Pair<TestEntity, TestDto> createTestDtoAndEntity(UUID idTest, String nameTest, long numberTest, Date dateTest,
                                                             Date startDate, Date endDate, long startNumber, long endNumber,
                                                             SimpleDto simpleDto, double longitude, double latitude) {
        Set<JoinTestEntity> joinTestEntities = new HashSet<>();
        Set<JoinTestDto> joinTestDtos = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            UUID id = UUID.randomUUID();
            String name = "join test " + i;
            JoinTestEntity joinTestEntity = new JoinTestEntity();
            JoinTestDto joinTestDto = new JoinTestDto();
            joinTestDto.setId(id);
            joinTestEntity.setId(id);

            joinTestDto.setName(name);
            joinTestEntity.setName(name);

            joinTestEntities.add(joinTestEntity);
            joinTestDtos.add(joinTestDto);
        }

        Set<ManyJoinEntity> manyJoinEntities = new HashSet<>();
        Set<ManyJoinDto> manyJoinDtos = new HashSet<>();
        for (long i = 0; i < 15; i++) {
            UUID id = UUID.randomUUID();
            String name = "many join test " + i;
            ManyJoinEntity manyJoinEntity = new ManyJoinEntity();
            ManyJoinDto manyJoinDto = new ManyJoinDto();
            manyJoinEntity.setId(id);
            manyJoinDto.setId(id);

            manyJoinEntity.setName(name);
            manyJoinDto.setName(name);

            manyJoinDto.setNumber(i);
            manyJoinEntity.setNumber(i);

            manyJoinEntities.add(manyJoinEntity);
            manyJoinDtos.add(manyJoinDto);
        }

        Set<UUID> elementsEntity = new HashSet<>();
        Set<UUID> elementsDto = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            UUID id = UUID.randomUUID();
            elementsDto.add(id);
            elementsEntity.add(id);
        }

        TestEntity testEntityPrepare = new TestEntity();
        testEntityPrepare.setManyJoinEntities(manyJoinEntities);
        testEntityPrepare.setJoinTestEntities(joinTestEntities);
        testEntityPrepare.setElements(elementsEntity);
        testEntityPrepare.setId(idTest);
        testEntityPrepare.setNumber(numberTest);
        testEntityPrepare.setName(nameTest);
        testEntityPrepare.setDate(dateTest);
        testEntityPrepare.setSimpleDto(simpleDto.getId());
        testEntityPrepare.setStartNumber(startNumber);
        testEntityPrepare.setStartDate(startDate);
        testEntityPrepare.setLongitude(longitude);
        testEntityPrepare.setLatitude(latitude);
        testEntityPrepare.setEndNumber(endNumber);
        testEntityPrepare.setEndDate(endDate);

        TestDto testDtoPrepare = new TestDto();
        testDtoPrepare.setManyJoinEntities(manyJoinDtos);
        testDtoPrepare.setJoinTestEntities(joinTestDtos);
        testDtoPrepare.setElements(elementsDto);
        testDtoPrepare.setNumber(numberTest);
        testDtoPrepare.setName(nameTest);
        testDtoPrepare.setDate(dateTest);
        testDtoPrepare.setId(idTest);
        testDtoPrepare.setSimpleDto(simpleDto);
        testDtoPrepare.setLongitude(longitude);
        testDtoPrepare.setLatitude(latitude);
        testDtoPrepare.setStartNumber(startNumber);
        testDtoPrepare.setEndNumber(endNumber);
        testDtoPrepare.setStartDate(startDate);
        testDtoPrepare.setEndDate(endDate);

        return Pair.of(testEntityPrepare, testDtoPrepare);
    }
}