package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.NotificationDto;
import org.go.together.model.Notification;
import org.go.together.service.interfaces.NotificationService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = RepositoryContext.class)
public class NotificationServiceTest extends CrudServiceCommonTest<Notification, NotificationDto> {
    @Test
    public void getNotificationByProducerIdTest() {
        NotificationDto createdDto = getCreatedEntityId(dto);

        NotificationDto notificationByProducerId = ((NotificationService) crudService).
                getNotificationByProducerId(createdDto.getProducerId());

        assertEquals(createdDto, notificationByProducerId);
    }

    @Override
    protected NotificationDto createDto() {
        return factory.manufacturePojo(NotificationDto.class);
    }
}
