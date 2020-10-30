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
class StringComparatorTest {
    public static final String TEST_STRING = "test string";
    public static final String ANOTHER_TEST_STRING = "another test string";
    private static final String FIELD = "field";
    @Autowired
    private Comparator<String> stringComparator;

    @Test
    void compareSameStrings() {
        Map<String, Object> compareResult = stringComparator.compare(FIELD, TEST_STRING, TEST_STRING);
        assertEquals(Collections.emptyMap(), compareResult);
    }

    @Test
    void compareDifferentStrings() {
        final Map<String, Object> expected = Map.of(FIELD, TEST_STRING + TO + ANOTHER_TEST_STRING);

        Map<String, Object> compareResult = stringComparator.compare(FIELD, TEST_STRING, ANOTHER_TEST_STRING);
        assertEquals(expected, compareResult);
    }
}