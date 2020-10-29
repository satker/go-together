package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.SimpleDto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.go.together.notification.dto.AnotherTestDto;
import org.go.together.notification.dto.TestComparableDto;
import org.go.together.notification.dto.TestNamedEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

import static org.go.together.notification.comparators.impl.DateComparatorTest.getThisDateAnotherMonth;
import static org.go.together.notification.comparators.impl.SimpleDtoComparatorTest.DIFFERENT_SIMPLE_DTO;
import static org.go.together.notification.comparators.impl.SimpleDtoComparatorTest.SIMPLE_DTO;
import static org.go.together.notification.comparators.impl.StringComparatorTest.ANOTHER_TEST_STRING;
import static org.go.together.notification.comparators.impl.StringComparatorTest.TEST_STRING;
import static org.go.together.notification.comparators.interfaces.Comparator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
class DtoComparatorTest {
    private static final String FIELD = "field";
    private static final String SECURE_FIELD = "secure field";
    private static final String ANOTHER_TEST_DTO_STRING = "another string";
    private static final Number ANOTHER_TEST_DTO_NUMBER = 2;
    private static final TestComparableDto TEST_DTO = createTestDto(new Date(), SIMPLE_DTO, TEST_STRING, TestNamedEnum.TEST);

    @Autowired
    private Comparator<TestComparableDto> testComparableDtoComparator;

    private static TestComparableDto createTestDto(Date date,
                                                   SimpleDto simpleDto,
                                                   String string,
                                                   TestNamedEnum testNamedEnum) {
        return TestComparableDto.builder()
                .id(UUID.randomUUID())
                .date(date)
                .number(Math.random())
                .simpleDto(simpleDto)
                .someObject(UUID.randomUUID())
                .string(string)
                .testNamedEnum(testNamedEnum)
                .anotherTestDto(createAnotherTestDto(ANOTHER_TEST_DTO_NUMBER, ANOTHER_TEST_DTO_STRING))
                .secureString(SECURE_FIELD).build();
    }

    private static AnotherTestDto createAnotherTestDto(Number number, String string) {
        return AnotherTestDto.builder()
                .id(UUID.randomUUID())
                .number(number)
                .string(string).build();
    }

    @Test
    void compareSameDtos() {
        String compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, TEST_DTO, false);

        assertEquals(StringUtils.EMPTY, compareResult);
    }

    @Test
    void compareDifferentSimpleDtoFields() {
        final TestComparableDto ANOTHER_TEST_DTO = createTestDto(getThisDateAnotherMonth(), DIFFERENT_SIMPLE_DTO,
                ANOTHER_TEST_STRING, TestNamedEnum.ANOTHER_TEST);

        final String changedField = CHANGED_WITH_UPPER_LETTER + StringUtils.SPACE + FIELD + COLON;
        final String someObjectChanged = "some object " + CHANGED;
        final String numberChanged = "number " + CHANGED + COLON + TEST_DTO.getNumber() + TO + ANOTHER_TEST_DTO.getNumber();
        final String dateChanged = "date " + CHANGED + COLON + TEST_DTO.getDate().toString() + TO + ANOTHER_TEST_DTO.getDate().toString();
        final String enumChanged = "test named enum " + CHANGED + COLON + TEST_DTO.getTestNamedEnum().getDescription() + TO +
                ANOTHER_TEST_DTO.getTestNamedEnum().getDescription();
        final String simpleDtoChanged = "simpleDto" + SimpleDtoComparator.NAME +
                StringUtils.SPACE + CHANGED + COLON + TEST_DTO.getSimpleDto().getName() + TO +
                ANOTHER_TEST_DTO.getSimpleDto().getName();
        final String stringChanged = "string " + CHANGED + COLON + TEST_DTO.getString() + TO + ANOTHER_TEST_DTO.getString();

        String compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, ANOTHER_TEST_DTO, false);

        assertTrue(compareResult.contains(changedField));
        assertTrue(compareResult.contains(someObjectChanged));
        assertTrue(compareResult.contains(numberChanged));
        assertTrue(compareResult.contains(dateChanged));
        assertTrue(compareResult.contains(enumChanged));
        assertTrue(compareResult.contains(simpleDtoChanged));
        assertTrue(compareResult.contains(stringChanged));

        String expectedCompareResultWithoutTrash = compareResult.replaceAll(changedField, StringUtils.EMPTY)
                .replaceAll(someObjectChanged, StringUtils.EMPTY)
                .replaceAll(numberChanged, StringUtils.EMPTY)
                .replaceAll(dateChanged, StringUtils.EMPTY)
                .replaceAll(enumChanged, StringUtils.EMPTY)
                .replaceAll(simpleDtoChanged, StringUtils.EMPTY)
                .replaceAll(stringChanged, StringUtils.EMPTY)
                .replaceAll(COMMA, StringUtils.EMPTY);

        assertEquals(expectedCompareResultWithoutTrash, StringUtils.EMPTY);
    }

    @Test
    void compareWithSecureString() {
        final TestComparableDto SECURE_FIELD_TEST_DTO = TEST_DTO.toBuilder().secureString("change " + SECURE_FIELD).build();
        final String changedField = CHANGED_WITH_UPPER_LETTER + StringUtils.SPACE + FIELD + COLON;
        final String secureFieldChanged = "secure string " + CHANGED;

        String compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, SECURE_FIELD_TEST_DTO, false);

        assertTrue(compareResult.contains(changedField));
        assertTrue(compareResult.contains(secureFieldChanged));

        String expectedCompareResultWithoutTrash = compareResult.replaceAll(changedField, StringUtils.EMPTY)
                .replaceAll(secureFieldChanged, StringUtils.EMPTY);

        assertEquals(expectedCompareResultWithoutTrash, StringUtils.EMPTY);
    }

    @Test
    void compareOneChangedFieldWithInnerComparingDto() {
        final String changedString = CHANGED + StringUtils.SPACE + ANOTHER_TEST_DTO_STRING;
        final TestComparableDto ANOTHER_DTO_TEST = TEST_DTO.toBuilder()
                .anotherTestDto(createAnotherTestDto(ANOTHER_TEST_DTO_NUMBER, changedString)).build();

        final String changedField = CHANGED_WITH_UPPER_LETTER + StringUtils.SPACE + FIELD + COLON;
        final String changedAnotherDtoField = CHANGED_WITH_UPPER_LETTER + StringUtils.SPACE + "another test dto" + COLON;
        final String anotherDtoStringFieldChanged = "another string " + CHANGED
                + COLON + ANOTHER_TEST_DTO_STRING + TO + changedString;

        String compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, ANOTHER_DTO_TEST, false);

        assertTrue(compareResult.contains(changedField));
        assertTrue(compareResult.contains(changedAnotherDtoField));
        assertTrue(compareResult.contains(anotherDtoStringFieldChanged));

        String expectedCompareResultWithoutTrash = compareResult.replaceAll(changedField, StringUtils.EMPTY)
                .replaceAll(changedAnotherDtoField, StringUtils.EMPTY)
                .replaceAll(anotherDtoStringFieldChanged, StringUtils.EMPTY);

        assertEquals(expectedCompareResultWithoutTrash, StringUtils.EMPTY);
    }

    @Test
    void compareMultipleChangedFieldWithInnerComparingDto() {
        final String changedString = CHANGED + StringUtils.SPACE + ANOTHER_TEST_DTO_STRING;
        final Number changedNumber = ANOTHER_TEST_DTO_NUMBER.intValue() + 1;
        final TestComparableDto ANOTHER_DTO_TEST = TEST_DTO.toBuilder()
                .anotherTestDto(createAnotherTestDto(changedNumber, changedString)).build();

        final String changedField = CHANGED_WITH_UPPER_LETTER + StringUtils.SPACE + FIELD + COLON;
        final String changedAnotherDtoField = CHANGED_WITH_UPPER_LETTER + StringUtils.SPACE + "another test dto" + COLON;
        final String anotherDtoStringFieldChanged = "another string " + CHANGED
                + COLON + ANOTHER_TEST_DTO_STRING + TO + changedString;
        final String anotherDtoNumberFieldChanged = "another number " + CHANGED
                + COLON + ANOTHER_TEST_DTO_NUMBER + TO + changedNumber;

        String compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, ANOTHER_DTO_TEST, false);

        assertTrue(compareResult.contains(changedField));
        assertTrue(compareResult.contains(changedAnotherDtoField));
        assertTrue(compareResult.contains(anotherDtoNumberFieldChanged));
        assertTrue(compareResult.contains(anotherDtoStringFieldChanged));

        String expectedCompareResultWithoutTrash = compareResult.replaceAll(changedField, StringUtils.EMPTY)
                .replaceAll(changedAnotherDtoField, StringUtils.EMPTY)
                .replaceAll(anotherDtoNumberFieldChanged, StringUtils.EMPTY)
                .replaceAll(anotherDtoStringFieldChanged, StringUtils.EMPTY)
                .replaceAll(COMMA, StringUtils.EMPTY);

        assertEquals(expectedCompareResultWithoutTrash, StringUtils.EMPTY);
    }
}