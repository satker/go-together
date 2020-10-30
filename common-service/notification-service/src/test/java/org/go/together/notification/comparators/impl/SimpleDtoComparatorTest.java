package org.go.together.notification.comparators.impl;

import org.go.together.dto.SimpleDto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Map;

import static org.go.together.notification.comparators.interfaces.Comparator.TO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
class SimpleDtoComparatorTest {
    public static final SimpleDto SIMPLE_DTO = new SimpleDto("test", "test");
    public static final SimpleDto DIFFERENT_SIMPLE_DTO = new SimpleDto("another test", "another test");
    private static final String FIELD = "field";
    private static final SimpleDto PART_SIMPLE_DTO = new SimpleDto("test", "another test");

    @Autowired
    private Comparator<SimpleDto> simpleDtoComparator;

    @Test
    void compareSameSimpleDtos() {
        Map<String, Object> compareResult = simpleDtoComparator.compare(FIELD, SIMPLE_DTO, SIMPLE_DTO);

        assertEquals(Collections.emptyMap(), compareResult);
    }

    @Test
    void compareDifferentSimpleDtos() {
        final Map<String, Object> expected = Map.of(FIELD + " name", SIMPLE_DTO.getName() + TO + DIFFERENT_SIMPLE_DTO.getName());

        Map<String, Object> compareResult = simpleDtoComparator.compare(FIELD, SIMPLE_DTO, DIFFERENT_SIMPLE_DTO);

        assertEquals(expected, compareResult);
    }

    @Test
    void comparePartlyDifferentSimpleDtos() {
        final Map<String, Object> expected = Map.of(FIELD + " name", SIMPLE_DTO.getName() + TO + PART_SIMPLE_DTO.getName());

        Map<String, Object> compareResult = simpleDtoComparator.compare(FIELD, SIMPLE_DTO, PART_SIMPLE_DTO);

        assertEquals(expected, compareResult);
    }
}