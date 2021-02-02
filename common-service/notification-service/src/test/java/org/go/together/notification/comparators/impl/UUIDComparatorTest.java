package org.go.together.notification.comparators.impl;

import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.context.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.go.together.notification.comparators.interfaces.Comparator.CHANGED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource(locations = "/application.properties")
public class UUIDComparatorTest {
    public static final UUID TEST_UUID = UUID.randomUUID();
    public static final UUID ANOTHER_TEST_UUID = UUID.randomUUID();
    private static final String FIELD = "field";
    @Autowired
    private Comparator<UUID> uuidComparator;

    @Test
    void compareSameUUIDs() {
        Map<String, Object> compareResult = uuidComparator.compare(FIELD, TEST_UUID, TEST_UUID);
        assertEquals(Collections.emptyMap(), compareResult);
    }

    @Test
    void compareDifferentUUIDs() {
        final Map<String, Object> expected = Map.of(FIELD, CHANGED);

        Map<String, Object> compareResult = uuidComparator.compare(FIELD, TEST_UUID, ANOTHER_TEST_UUID);
        assertEquals(expected, compareResult);
    }
}
