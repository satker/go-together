package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.SimpleDto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.go.together.notification.comparators.interfaces.Comparator.FROM;
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
        String compareResult = simpleDtoComparator.compare(FIELD, SIMPLE_DTO, SIMPLE_DTO, false);

        assertEquals(StringUtils.EMPTY, compareResult);
    }

    @Test
    void compareDifferentSimpleDtos() {
        final String result = FIELD + " name" + FROM + SIMPLE_DTO.getName() + TO + DIFFERENT_SIMPLE_DTO.getName();

        String compareResult = simpleDtoComparator.compare(FIELD, SIMPLE_DTO, DIFFERENT_SIMPLE_DTO, false);

        assertEquals(result, compareResult);
    }

    @Test
    void comparePartlyDifferentSimpleDtos() {
        final String result = FIELD + " name" + FROM + SIMPLE_DTO.getName() + TO + PART_SIMPLE_DTO.getName();

        String compareResult = simpleDtoComparator.compare(FIELD, SIMPLE_DTO, PART_SIMPLE_DTO, false);

        assertEquals(result, compareResult);
    }
}