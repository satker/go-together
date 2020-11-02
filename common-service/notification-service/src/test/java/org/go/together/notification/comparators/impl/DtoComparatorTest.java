package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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

import java.util.*;

import static org.go.together.notification.comparators.impl.DateComparatorTest.getThisDateAnotherMonth;
import static org.go.together.notification.comparators.impl.SimpleDtoComparatorTest.DIFFERENT_SIMPLE_DTO;
import static org.go.together.notification.comparators.impl.SimpleDtoComparatorTest.SIMPLE_DTO;
import static org.go.together.notification.comparators.impl.StringComparatorTest.ANOTHER_TEST_STRING;
import static org.go.together.notification.comparators.impl.StringComparatorTest.TEST_STRING;
import static org.go.together.notification.comparators.interfaces.Comparator.CHANGED;
import static org.go.together.notification.comparators.interfaces.Comparator.TO;
import static org.junit.jupiter.api.Assertions.*;

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
        return createTestDto(date, simpleDto, string, testNamedEnum, UUID.randomUUID());
    }

    private static TestComparableDto createTestDto(Date date,
                                                   SimpleDto simpleDto,
                                                   String string,
                                                   TestNamedEnum testNamedEnum,
                                                   UUID anotherTestDtoId) {
        AnotherTestDto firstTestDto = createAnotherTestDto(anotherTestDtoId, ANOTHER_TEST_DTO_NUMBER, ANOTHER_TEST_DTO_STRING + 1);
        AnotherTestDto secondTestDto = createAnotherTestDto(ANOTHER_TEST_DTO_NUMBER, ANOTHER_TEST_DTO_STRING + 2);
        return TestComparableDto.builder()
                .id(UUID.randomUUID())
                .date(date)
                .number(Math.random())
                .simpleDto(simpleDto)
                .someObject(UUID.randomUUID())
                .string(string)
                .testNamedEnum(testNamedEnum)
                .anotherTestDto(secondTestDto)
                .anotherTestDtoWithIdCompare(firstTestDto)
                .secureString(SECURE_FIELD).build();
    }

    public static AnotherTestDto createAnotherTestDto(Number number, String string) {
        return createAnotherTestDto(UUID.randomUUID(), number, string);
    }

    private static AnotherTestDto createAnotherTestDto(UUID id, Number number, String string) {
        return AnotherTestDto.builder()
                .id(id)
                .number(number)
                .string(string).build();
    }

    public static void compareMaps(Map<String, Object> result, Pair<String, Object> changedField) {
        Object changedFieldInResult = result.get(changedField.getKey());
        assertNotNull(changedFieldInResult);
        if (changedFieldInResult instanceof Collection) {
            assertArrayEquals(((Collection<?>) changedField.getValue()).stream().sorted().toArray(),
                    ((Collection<?>) changedFieldInResult).stream().sorted().toArray());
        } else {
            assertEquals(changedField.getValue(), changedFieldInResult);
        }
    }

    @Test
    void compareSameDtos() {
        Map<String, Object> compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, TEST_DTO);

        assertEquals(Collections.emptyMap(), compareResult);
    }

    @Test
    void compareDifferentSimpleDtoFields() {
        final TestComparableDto ANOTHER_TEST_DTO = createTestDto(getThisDateAnotherMonth(), DIFFERENT_SIMPLE_DTO,
                ANOTHER_TEST_STRING, TestNamedEnum.ANOTHER_TEST, TEST_DTO.getAnotherTestDtoWithIdCompare().getId());

        final Pair<String, Object> someObjectChanged = Pair.of("some object", CHANGED);
        final Pair<String, Object> numberChanged = Pair.of("number", TEST_DTO.getNumber() + TO + ANOTHER_TEST_DTO.getNumber());
        final Pair<String, Object> dateChanged = Pair.of("date", TEST_DTO.getDate().toString() + TO + ANOTHER_TEST_DTO.getDate().toString());
        final Pair<String, Object> enumChanged = Pair.of("test named enum", TEST_DTO.getTestNamedEnum().getDescription() + TO +
                ANOTHER_TEST_DTO.getTestNamedEnum().getDescription());
        final Pair<String, Object> simpleDtoChanged = Pair.of("simpleDto" + SimpleDtoComparator.NAME,
                TEST_DTO.getSimpleDto().getName() + TO + ANOTHER_TEST_DTO.getSimpleDto().getName());
        final Pair<String, Object> stringChanged = Pair.of("string", TEST_DTO.getString() + TO + ANOTHER_TEST_DTO.getString());

        Map<String, Object> compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, ANOTHER_TEST_DTO);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(6, result.size());

        compareMaps(result, someObjectChanged);
        compareMaps(result, numberChanged);
        compareMaps(result, dateChanged);
        compareMaps(result, enumChanged);
        compareMaps(result, simpleDtoChanged);
        compareMaps(result, stringChanged);
    }

    @Test
    void compareWithSecureString() {
        final TestComparableDto SECURE_FIELD_TEST_DTO = TEST_DTO.toBuilder().secureString("change " + SECURE_FIELD).build();
        final Pair<String, Object> secureFieldChanged = Pair.of("secure string", CHANGED);

        Map<String, Object> compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, SECURE_FIELD_TEST_DTO, null);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(1, result.size());

        compareMaps(result, secureFieldChanged);
    }

    @Test
    void compareOneChangedFieldWithInnerComparingDto() {
        final String changedString = CHANGED + StringUtils.SPACE + ANOTHER_TEST_DTO_STRING;
        final TestComparableDto ANOTHER_DTO_TEST = TEST_DTO.toBuilder()
                .anotherTestDto(createAnotherTestDto(ANOTHER_TEST_DTO_NUMBER, changedString)).build();

        final String anotherTestDto = "another test dto";
        final Pair<String, Object> anotherDtoStringFieldChanged = Pair.of("another string",
                TEST_DTO.getAnotherTestDto().getString() + TO + changedString);

        Map<String, Object> compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, ANOTHER_DTO_TEST);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(1, result.size());

        compareMaps((Map<String, Object>) result.get(anotherTestDto), anotherDtoStringFieldChanged);
    }

    @Test
    void compareMultipleChangedFieldWithInnerComparingDto() {
        final String changedString = CHANGED + StringUtils.SPACE + ANOTHER_TEST_DTO_STRING;
        final Number changedNumber = ANOTHER_TEST_DTO_NUMBER.intValue() + 1;
        final TestComparableDto ANOTHER_DTO_TEST = TEST_DTO.toBuilder()
                .anotherTestDto(createAnotherTestDto(changedNumber, changedString)).build();

        final String anotherTestDto = "another test dto";
        final Pair<String, Object> anotherDtoStringFieldChanged = Pair.of("another string",
                TEST_DTO.getAnotherTestDto().getString() + TO + changedString);
        final Pair<String, Object> anotherDtoNumberFieldChanged = Pair.of("another number",
                ANOTHER_TEST_DTO_NUMBER + TO + changedNumber);

        Map<String, Object> compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, ANOTHER_DTO_TEST);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(1, result.size());

        Map<String, Object> innerDtoComparingMap = (Map<String, Object>) result.get(anotherTestDto);

        compareMaps(innerDtoComparingMap, anotherDtoStringFieldChanged);
        compareMaps(innerDtoComparingMap, anotherDtoNumberFieldChanged);
    }

    @Test
    void compareDtosIdCompare() {
        final TestComparableDto ANOTHER_TEST_DTO = TEST_DTO.toBuilder()
                .anotherTestDtoWithIdCompare(createAnotherTestDto(3, "test")).build();

        final Pair<String, Object> idCompareFieldChanged = Pair.of("another test dto with id compare", CHANGED);

        Map<String, Object> compareResult = testComparableDtoComparator.compare(FIELD, TEST_DTO, ANOTHER_TEST_DTO);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(1, result.size());

        compareMaps(result, idCompareFieldChanged);
    }
}