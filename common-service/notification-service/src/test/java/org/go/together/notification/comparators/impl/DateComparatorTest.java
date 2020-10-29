package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Date;

import static org.go.together.notification.comparators.interfaces.Comparator.FROM;
import static org.go.together.notification.comparators.interfaces.Comparator.TO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
class DateComparatorTest {
    private static final String FIELD = "field";
    private static final Date TEST_DATE = new Date();
    private static final Date ANOTHER_TEST_DATE = getThisDateAnotherMonth();
    @Autowired
    private Comparator<Date> dateComparator;

    public static Date getThisDateAnotherMonth() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MONTH, 1);
        return instance.getTime();
    }

    @Test
    void compareSameEnums() {
        String compareResult = dateComparator.compare(FIELD, TEST_DATE, TEST_DATE, false);
        assertEquals(StringUtils.EMPTY, compareResult);
    }

    @Test
    void compareDifferentEnums() {
        final String result = FIELD + FROM + TEST_DATE.toString() + TO + ANOTHER_TEST_DATE.toString();

        String compareResult = dateComparator.compare(FIELD, TEST_DATE, ANOTHER_TEST_DATE, false);
        assertEquals(result, compareResult);
    }
}