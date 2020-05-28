package org.go.together.logic.validation;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.SimpleDto;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;
import org.go.together.test.validation.TestValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryContext.class)
class ValidatorTest {
    TestDto testDto;
    @Autowired
    private TestValidator testValidator;

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
    }

    @AfterEach
    public void clean() {
        testDto = null;
    }

    @Test
    void validateSuccess() {
        String validate = testValidator.validate(testDto);

        assertTrue(StringUtils.isBlank(validate));
    }

    @Test
    void validateWithIncorrectDates() {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(testDto.getDate());
        endCalendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = endCalendar.getTime();
        testDto.setEndDate(endDate);

        String validate = testValidator.validate(testDto);

        assertTrue(validate.contains("test dates"));
    }

    @Test
    void validateWithIncorrectNumber() {
        testDto.setNumber(0);

        String validate = testValidator.validate(testDto);

        assertTrue(validate.contains("test number"));
    }

    @Test
    void validateWithIncorrectNumbers() {
        testDto.setEndNumber(0);

        String validate = testValidator.validate(testDto);

        assertTrue(validate.contains("test number interval"));
    }

    @Test
    void validateWithNullTestId() {
        testDto.setId(null);

        String validate = testValidator.validate(testDto);

        assertTrue(validate.contains("test id"));
    }

    @Test
    void validateWithNullSimpleDtoId() {
        testDto.setSimpleDto(new SimpleDto(null, "string"));

        String validate = testValidator.validate(testDto);

        assertTrue(validate.contains("simple dto"));
    }

    @Test
    void validateWithEmptyName() {
        testDto.setName(EMPTY);

        String validate = testValidator.validate(testDto);

        assertTrue(validate.contains("test name"));
    }

    private TestDto createTestDto(UUID idTest, String nameTest, long numberTest, Date dateTest,
                                  Date startDate, Date endDate, long startNumber, long endNumber,
                                  SimpleDto simpleDto, double longitude, double latitude) {
        Set<JoinTestDto> joinTestDtos = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            UUID id = UUID.randomUUID();
            String name = "join test " + i;
            JoinTestDto joinTestDto = new JoinTestDto();
            joinTestDto.setId(id);
            joinTestDto.setName(name);
            joinTestDtos.add(joinTestDto);
        }

        Set<ManyJoinDto> manyJoinDtos = new HashSet<>();
        for (int i = 0; i < 15; i++) {
            UUID id = UUID.randomUUID();
            String name = "many join test " + i;
            ManyJoinDto manyJoinDto = new ManyJoinDto();
            manyJoinDto.setId(id);
            manyJoinDto.setName(name);
            manyJoinDto.setNumber(i);
            manyJoinDtos.add(manyJoinDto);
        }

        Set<String> elementsDto = new HashSet<>();
        for (int i = 0; i < 20; i++) {
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