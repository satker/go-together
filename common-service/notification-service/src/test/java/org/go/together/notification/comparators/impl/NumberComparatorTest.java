package org.go.together.notification.comparators.impl;

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
class NumberComparatorTest {
    private static final String FIELD = "field";
    private static final Double TEST_NUMBER = Math.random();
    private static final Double ANOTHER_TEST_NUMBER = Math.random();

    @Autowired
    private Comparator<Number> numberComparator;

    @Test
    void compareSameNumbers() {
        Map<String, Object> compareResult = numberComparator.compare(FIELD, TEST_NUMBER, TEST_NUMBER);
        assertEquals(Collections.emptyMap(), compareResult);
    }

    @Test
    void compareDifferentNumbers() {
        final Map<String, Object> result = Map.of(FIELD, TEST_NUMBER.toString() + TO + ANOTHER_TEST_NUMBER.toString());

        Map<String, Object> compareResult = numberComparator.compare(FIELD, TEST_NUMBER, ANOTHER_TEST_NUMBER);
        assertEquals(result, compareResult);
    }
}