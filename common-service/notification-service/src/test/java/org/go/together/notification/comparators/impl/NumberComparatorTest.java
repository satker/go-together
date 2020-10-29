package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
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
class NumberComparatorTest {
    private static final String FIELD = "field";
    private static final Double TEST_NUMBER = Math.random();
    private static final Double ANOTHER_TEST_NUMBER = Math.random();

    @Autowired
    private Comparator<Number> numberComparator;

    @Test
    void compareSameNumbers() {
        String compareResult = numberComparator.compare(FIELD, TEST_NUMBER, TEST_NUMBER, false);
        assertEquals(StringUtils.EMPTY, compareResult);
    }

    @Test
    void compareDifferentNumbers() {
        final String result = FIELD + FROM + TEST_NUMBER.toString() + TO + ANOTHER_TEST_NUMBER.toString();

        String compareResult = numberComparator.compare(FIELD, TEST_NUMBER, ANOTHER_TEST_NUMBER, false);
        assertEquals(result, compareResult);
    }
}