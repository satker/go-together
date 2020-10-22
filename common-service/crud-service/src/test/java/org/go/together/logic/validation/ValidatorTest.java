package org.go.together.logic.validation;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.SimpleDto;
import org.go.together.test.dto.TestDto;
import org.go.together.validation.Validator;
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
import static org.go.together.test.TestUtils.createTestDto;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryContext.class)
class ValidatorTest {
    @Autowired
    private Validator<TestDto> testValidator;

    TestDto testDto;

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
        String validate = testValidator.validate(testDto, null);

        assertTrue(StringUtils.isBlank(validate));
    }

    @Test
    void validateWithIncorrectDates() {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(testDto.getDate());
        endCalendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = endCalendar.getTime();
        testDto.setEndDate(endDate);

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("test dates"));
    }

    @Test
    void validateWithIncorrectNumber() {
        testDto.setNumber(0);

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("test number"));
    }

    @Test
    void validateWithIncorrectNumbers() {
        testDto.setEndNumber(0);

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("test number interval"));
    }

    @Test
    void validateWithNullTestId() {
        testDto.setId(null);

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("test id"));
    }

    @Test
    void validateWithNullSimpleDtoId() {
        testDto.setSimpleDto(new SimpleDto(null, "string"));

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("simple dto"));
    }

    @Test
    void validateWithEmptyName() {
        testDto.setName(EMPTY);

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("test name"));
    }

    @Test
    void validateWithEmptyNameJoinTest() {
        testDto.getJoinTestEntities().forEach(joinTestDto -> joinTestDto.setName(EMPTY));

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("test name"));
    }

    @Test
    void validateWithEmptyCollection() {
        testDto.setJoinTestEntities(Collections.emptySet());

        String validate = testValidator.validate(testDto, null);

        assertTrue(validate.contains("test join tests"));
    }

    @Test
    void validateDtoCollection() {
        UUID id = UUID.randomUUID();
        String name = "test1 name";
        long number = 11;
        Date date = new Date();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.add(Calendar.MONTH, 2);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startDate);
        endCalendar.add(Calendar.MONTH, 2);
        Date endDate = endCalendar.getTime();
        long startNumber = 11;
        long endNumber = 32;
        double latitude = 11.313230192867607;
        double longitude = 75.39449363632201;
        SimpleDto simpleDto = new SimpleDto("simpleDto1", "simpleDto1");

        TestDto testDtoAnother = createTestDto(id, name, number, date, startDate, endDate,
                startNumber, endNumber, simpleDto, longitude, latitude);

        String validate = testValidator.validateDtos(Set.of(testDto, testDtoAnother), null);

        assertTrue(StringUtils.isBlank(validate));
    }
}