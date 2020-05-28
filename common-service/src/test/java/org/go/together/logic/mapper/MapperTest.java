package org.go.together.logic.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.context.RepositoryContext;
import org.go.together.interfaces.Identified;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.mapper.TestMapper;
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
    TestDto testDto;
    TestEntity testEntity;
    @Autowired
    private TestMapper testMapper;

    @BeforeEach
    void init() {
        UUID id = UUID.randomUUID();
        String name = "test name";
        long number = 6;
        Date date = new Date();

        Pair<TestEntity, TestDto> testDtoAndEntity = createTestDtoAndEntity(id, name, number, date);

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

        Pair<TestEntity, TestDto> testDtoAndEntity = createTestDtoAndEntity(id, name, number, date);

        Collection<TestDto> testDtos = Arrays.asList(testDto, testDtoAndEntity.getRight());
        Collection<TestEntity> testEntities = Arrays.asList(testEntity, testDtoAndEntity.getLeft());

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

        Pair<TestEntity, TestDto> testDtoAndEntity = createTestDtoAndEntity(id, name, number, date);

        Collection<TestDto> testDtos = Arrays.asList(testDto, testDtoAndEntity.getRight());
        Collection<TestEntity> testEntities = Arrays.asList(testEntity, testDtoAndEntity.getLeft());

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


    private Pair<TestEntity, TestDto> createTestDtoAndEntity(UUID idTest, String nameTest, long numberTest, Date dateTest) {
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
        for (int i = 0; i < 15; i++) {
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
        Set<String> elementsDto = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            UUID id = UUID.randomUUID();
            elementsDto.add(id.toString());
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

        TestDto testDtoPrepare = new TestDto();
        testDtoPrepare.setManyJoinEntities(manyJoinDtos);
        testDtoPrepare.setJoinTestEntities(joinTestDtos);
        testDtoPrepare.setElements(elementsDto);
        testDtoPrepare.setNumber(numberTest);
        testDtoPrepare.setName(nameTest);
        testDtoPrepare.setDate(dateTest);
        testDtoPrepare.setId(idTest);

        return Pair.of(testEntityPrepare, testDtoPrepare);
    }
}