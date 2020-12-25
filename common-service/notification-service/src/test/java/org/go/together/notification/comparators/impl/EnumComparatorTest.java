package org.go.together.notification.comparators.impl;

import org.go.together.interfaces.NamedEnum;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.go.together.notification.dto.TestNamedEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Map;

import static org.go.together.notification.comparators.interfaces.Comparator.TO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource(locations = "/application.properties")
class EnumComparatorTest {
    private static final String FIELD = "field";
    private static final TestNamedEnum TEST_NAMED_ENUM = TestNamedEnum.TEST;
    private static final TestNamedEnum ANOTHER_TEST_NAMED_ENUM = TestNamedEnum.ANOTHER_TEST;

    @Autowired
    private Comparator<NamedEnum> namedEnumComparator;

    @Test
    void compareSameEnums() {
        Map<String, Object> compareResult = namedEnumComparator.compare(FIELD, TEST_NAMED_ENUM, TEST_NAMED_ENUM);
        assertEquals(Collections.emptyMap(), compareResult);
    }

    @Test
    void compareDifferentEnums() {
        final Map<String, Object> result = Map.of(FIELD, TEST_NAMED_ENUM.getDescription() + TO + ANOTHER_TEST_NAMED_ENUM.getDescription());

        Map<String, Object> compareResult = namedEnumComparator.compare(FIELD, TEST_NAMED_ENUM, ANOTHER_TEST_NAMED_ENUM);
        assertEquals(result, compareResult);
    }
}