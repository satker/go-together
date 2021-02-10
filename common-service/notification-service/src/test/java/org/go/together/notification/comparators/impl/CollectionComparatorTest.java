package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.FieldProperties;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.go.together.notification.dto.AnotherTestDto;
import org.go.together.notification.dto.TestComparableDto;
import org.go.together.utils.NotificationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.go.together.notification.comparators.impl.DtoComparatorTest.compareMaps;
import static org.go.together.notification.comparators.impl.DtoComparatorTest.createAnotherTestDto;
import static org.go.together.notification.comparators.interfaces.Comparator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource(locations = "/application.properties")
class CollectionComparatorTest {
    private static final FieldProperties COMPARING_OBJECT = NotificationUtils.getComparingMap(TestComparableDto.class)
            .get("test dtos");
    private static final String ANOTHER_TEST_DTO_STRING = "another string";
    private static final Number ANOTHER_TEST_DTO_NUMBER = 2;
    private static final String FIELD = "test dtos";
    private static final AnotherTestDto FIRST_TEST_DTO =
            createAnotherTestDto(ANOTHER_TEST_DTO_NUMBER, ANOTHER_TEST_DTO_STRING + 1);
    private static final AnotherTestDto SECOND_TEST_DTO =
            createAnotherTestDto(ANOTHER_TEST_DTO_NUMBER, ANOTHER_TEST_DTO_STRING + 2);
    private static final Set<AnotherTestDto> TEST_DTOS = Set.of(FIRST_TEST_DTO, SECOND_TEST_DTO);

    @Autowired
    private Comparator<Collection<?>> collectionComparator;

    @Test
    void compareNotChangedElementsCollections() {
        Map<String, Object> compareResult = collectionComparator.compare(FIELD, TEST_DTOS, TEST_DTOS, COMPARING_OBJECT);

        assertEquals(0, compareResult.size());
    }

    @Test
    void compareDtoAddedElementsCollections() {
        List<String> expectedAddedElements = TEST_DTOS.stream()
                .map(ComparableDto::getMainField)
                .collect(Collectors.toList());
        final Pair<String, Object> expectedChangedCollections = Pair.of(ADDED, expectedAddedElements);

        Map<String, Object> compareResult = collectionComparator.compare(FIELD, Collections.emptySet(), TEST_DTOS, COMPARING_OBJECT);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(1, result.size());

        compareMaps(result, expectedChangedCollections);
    }

    @Test
    void compareDtoRemovedElementsCollections() {
        List<String> expectedRemovedElements = TEST_DTOS.stream()
                .map(ComparableDto::getMainField)
                .collect(Collectors.toList());
        final Pair<String, Object> expectedChangedCollections = Pair.of(REMOVED, expectedRemovedElements);

        Map<String, Object> compareResult = collectionComparator.compare(FIELD, TEST_DTOS, null, COMPARING_OBJECT);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(1, result.size());

        compareMaps(result, expectedChangedCollections);
    }

    @Test
    void compareDtoAddedAndRemovedElementsCollections() {
        Set<AnotherTestDto> anotherTestDtos = Set.of(createAnotherTestDto(3, "test3"),
                createAnotherTestDto(4, "test4"));
        List<String> expectedRemovedElements = TEST_DTOS.stream()
                .map(ComparableDto::getMainField)
                .collect(Collectors.toList());
        List<String> expectedAddedElements = anotherTestDtos.stream()
                .map(ComparableDto::getMainField)
                .collect(Collectors.toList());
        final Pair<String, Object> expectedRemovedElementsCollections = Pair.of(REMOVED, expectedRemovedElements);
        final Pair<String, Object> expectedAddedElementsCollections = Pair.of(ADDED, expectedAddedElements);

        Map<String, Object> compareResult = collectionComparator.compare(FIELD, TEST_DTOS, anotherTestDtos, COMPARING_OBJECT);

        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(2, result.size());

        compareMaps(result, expectedRemovedElementsCollections);
        compareMaps(result, expectedAddedElementsCollections);
    }

    @Test
    void compareDtoChangedElementsCollections() {
        AnotherTestDto changedElement = createAnotherTestDto(FIRST_TEST_DTO.getId(), 123,
                FIRST_TEST_DTO.getString());
        AnotherTestDto nextChangedElement = createAnotherTestDto(SECOND_TEST_DTO.getId(), SECOND_TEST_DTO.getNumber(),
                "changed element");
        Set<AnotherTestDto> changedTestDtos = Set.of(changedElement, nextChangedElement);
        Map<String, Object> compareResult = collectionComparator.compare(FIELD, TEST_DTOS, changedTestDtos, COMPARING_OBJECT);

        final Pair<String, Object> numberChanged = Pair.of("another number",
                FIRST_TEST_DTO.getNumber() + TO + changedElement.getNumber());
        final Pair<String, Object> stringChanged = Pair.of("another string",
                SECOND_TEST_DTO.getString() + TO + nextChangedElement.getString());


        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(1, result.size());

        Map<String, Object> innerDtoComparingMap = (Map<String, Object>) result.get(CHANGED);

        assertEquals(2, innerDtoComparingMap.size());
        compareMaps((Map<String, Object>) innerDtoComparingMap.get(FIRST_TEST_DTO.getString()), numberChanged);
        compareMaps((Map<String, Object>) innerDtoComparingMap.get(SECOND_TEST_DTO.getString()), stringChanged);
    }

    @Test
    void compareDtoChangedElementsCollectionsIdCompare() {
        final String TEST_ADDED_STRING = "added element";
        AnotherTestDto changedElement = createAnotherTestDto(FIRST_TEST_DTO.getId(), 123,
                FIRST_TEST_DTO.getString());
        AnotherTestDto nextChangedElement = createAnotherTestDto(null, SECOND_TEST_DTO.getNumber(), TEST_ADDED_STRING);
        Set<AnotherTestDto> changedTestDtos = Set.of(changedElement, nextChangedElement);

        FieldProperties idCompareFieldProperties = COMPARING_OBJECT.toBuilder().idCompare(true).build();
        Map<String, Object> compareResult = collectionComparator.compare(FIELD, TEST_DTOS, changedTestDtos, idCompareFieldProperties);

        final Pair<String, Object> removedElement = Pair.of(REMOVED, List.of(SECOND_TEST_DTO.getString()));
        final Pair<String, Object> addedElement = Pair.of(ADDED, List.of(TEST_ADDED_STRING));


        assertEquals(1, compareResult.size());

        Map<String, Object> result = (Map<String, Object>) compareResult.get(FIELD);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(removedElement.getValue(), result.get(removedElement.getKey()));
        assertEquals(addedElement.getValue(), result.get(addedElement.getKey()));
    }
}