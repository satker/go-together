package org.go.together.test;

import org.go.together.dto.SimpleDto;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TestUtils {
    public static TestDto createTestDto(UUID idTest, String nameTest, long numberTest, Date dateTest,
                                        Date startDate, Date endDate, long startNumber, long endNumber,
                                        SimpleDto simpleDto, double longitude, double latitude) {
        Set<JoinTestDto> joinTestDtos = new HashSet<>();
        for (int i = 1; i < 10; i++) {
            UUID id = UUID.randomUUID();
            String name = "join test " + i;
            JoinTestDto joinTestDto = new JoinTestDto();
            joinTestDto.setId(id);
            joinTestDto.setName(name);
            joinTestDtos.add(joinTestDto);
        }

        Set<ManyJoinDto> manyJoinDtos = new HashSet<>();
        for (int i = 1; i < 15; i++) {
            UUID id = UUID.randomUUID();
            String name = "many join test " + i;
            ManyJoinDto manyJoinDto = new ManyJoinDto();
            manyJoinDto.setId(id);
            manyJoinDto.setName(name);
            manyJoinDto.setNumber(i);
            manyJoinDtos.add(manyJoinDto);
        }

        Set<String> elementsDto = new HashSet<>();
        for (int i = 1; i < 20; i++) {
            UUID id = UUID.randomUUID();
            elementsDto.add(id.toString());
        }

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
}
