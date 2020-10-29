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
class StringComparatorTest {
    public static final String TEST_STRING = "test string";
    public static final String ANOTHER_TEST_STRING = "another test string";
    private static final String FIELD = "field";
    @Autowired
    private Comparator<String> stringComparator;

    @Test
    void compareSameStrings() {
        String compareResult = stringComparator.compare(FIELD, TEST_STRING, TEST_STRING, false);
        assertEquals(StringUtils.EMPTY, compareResult);
    }

    @Test
    void compareDifferentStrings() {
        final String result = FIELD + FROM + TEST_STRING + TO + ANOTHER_TEST_STRING;

        String compareResult = stringComparator.compare(FIELD, TEST_STRING, ANOTHER_TEST_STRING, false);
        assertEquals(result, compareResult);
    }
}