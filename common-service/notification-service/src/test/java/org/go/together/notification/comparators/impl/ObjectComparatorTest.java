package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.go.together.notification.comparators.interfaces.Comparator.CHANGED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
class ObjectComparatorTest {
    private static final String FIELD = "field";
    private static final UUID TEST_OBJECT = UUID.randomUUID();
    private static final UUID ANOTHER_TEST_OBJECT = UUID.randomUUID();

    @Autowired
    private Comparator<Object> objectComparator;

    @Test
    void compareSameObjects() {
        String compareResult = objectComparator.compare(FIELD, TEST_OBJECT, TEST_OBJECT, false);
        assertEquals(StringUtils.EMPTY, compareResult);
    }

    @Test
    void compareDifferentObjects() {
        final String result = FIELD + StringUtils.SPACE + CHANGED;

        String compareResult = objectComparator.compare(FIELD, TEST_OBJECT, ANOTHER_TEST_OBJECT, false);
        assertEquals(result, compareResult);
    }
}