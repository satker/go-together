package org.go.together.validation.test;

import org.go.together.dto.SimpleDto;
import org.go.together.validation.test.dto.JoinTestDto;
import org.go.together.validation.test.dto.ManyJoinDto;
import org.go.together.validation.test.dto.TestDto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TestUtils {
    public static TestDto createTestDto(UUID idTest, String nameTest, long numberTest, Date dateTest,
                                        Date startDate, Date endDate, long startNumber, long endNumber,
                                        SimpleDto simpleDto, double longitude, double latitude) {
        Set<JoinTestDto> joinTestDtos = createJoinTestDtos();

        Set<ManyJoinDto> manyJoinDtos = createManyJoinDtos();

        Set<UUID> elementsDto = createElementDtos();

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

        return testDtoPrepare;
    }

    private static Set<UUID> createElementDtos() {
        Set<UUID> elementsDto = new HashSet<>();
        for (int i = 1; i < 20; i++) {
            UUID id = UUID.randomUUID();
            elementsDto.add(id);
        }
        return elementsDto;
    }

    private static Set<JoinTestDto> createJoinTestDtos() {
        Set<JoinTestDto> joinTestDtos = new HashSet<>();
        for (int i = 1; i < 10; i++) {
            UUID id = UUID.randomUUID();
            String name = "join test " + i;
            JoinTestDto joinTestDto = new JoinTestDto();
            joinTestDto.setId(id);
            joinTestDto.setName(name);
            joinTestDtos.add(joinTestDto);
        }
        return joinTestDtos;
    }

    public static Set<ManyJoinDto> createManyJoinDtos() {
        Set<ManyJoinDto> manyJoinDtos = new HashSet<>();
        for (int i = 1; i < 15; i++) {
            UUID id = UUID.randomUUID();
            String name = "many join test " + i;
            ManyJoinDto manyJoinDto = new ManyJoinDto();
            manyJoinDto.setId(id);
            manyJoinDto.setName(name);
            manyJoinDto.setNumber((long) i);
            manyJoinDtos.add(manyJoinDto);
        }
        return manyJoinDtos;
    }
}
