package org.go.together.notification.comparators.impl;

import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

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
        Map<String, Object> compareResult = dateComparator.compare(FIELD, TEST_DATE, TEST_DATE);
        assertEquals(Collections.emptyMap(), compareResult);
    }

    @Test
    void compareDifferentEnums() {
        final Map<String, Object> result = Map.of(FIELD, TEST_DATE.toString() + TO + ANOTHER_TEST_DATE.toString());

        Map<String, Object> compareResult = dateComparator.compare(FIELD, TEST_DATE, ANOTHER_TEST_DATE);
        assertEquals(result, compareResult);
    }
}