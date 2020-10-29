package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.NamedEnum;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.go.together.notification.dto.TestNamedEnum;
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
class EnumComparatorTest {
    private static final String FIELD = "field";
    private static final TestNamedEnum TEST_NAMED_ENUM = TestNamedEnum.TEST;
    private static final TestNamedEnum ANOTHER_TEST_NAMED_ENUM = TestNamedEnum.ANOTHER_TEST;

    @Autowired
    private Comparator<NamedEnum> namedEnumComparator;

    @Test
    void compareSameEnums() {
        String compareResult = namedEnumComparator.compare(FIELD, TEST_NAMED_ENUM, TEST_NAMED_ENUM, false);
        assertEquals(StringUtils.EMPTY, compareResult);
    }

    @Test
    void compareDifferentEnums() {
        final String result = FIELD + FROM + TEST_NAMED_ENUM.getDescription() + TO + ANOTHER_TEST_NAMED_ENUM.getDescription();

        String compareResult = namedEnumComparator.compare(FIELD, TEST_NAMED_ENUM, ANOTHER_TEST_NAMED_ENUM, false);
        assertEquals(result, compareResult);
    }
}