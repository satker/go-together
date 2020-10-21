package org.go.together.service;

import org.go.together.client.UserClient;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.EventUserDto;
import org.go.together.model.Event;
import org.go.together.model.EventUser;
import org.go.together.repository.interfaces.EventRepository;
import org.go.together.service.interfaces.EventUserService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
public class EventUserServiceTest extends CrudServiceCommonTest<EventUser, EventUserDto> {
    @Autowired
    private UserClient userClient;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    public void init() {
        super.init();
        when(userClient.checkIfUserPresentsById(dto.getUser().getId())).thenReturn(true);
        when(userClient.checkIfUserPresentsById(updatedDto.getUser().getId())).thenReturn(true);
        when(userClient.findSimpleUserDtosByUserIds(Collections.singleton(dto.getUser().getId())))
                .thenReturn(Collections.singleton(dto.getUser()));
        when(userClient.findSimpleUserDtosByUserIds(Collections.singleton(updatedDto.getUser().getId())))
                .thenReturn(Collections.singleton(updatedDto.getUser()));
        Event entity = new Event();
        entity.setId(dto.getEventId());
        eventRepository.save(entity);
        entity.setId(updatedDto.getEventId());
        eventRepository.save(entity);
    }

    @Test
    public void deleteEventUserByEventIdTest() {
        crudService.create(dto);
        boolean isDeleted = ((EventUserService) crudService).deleteEventUserByEventId(dto);

        assertTrue(isDeleted);
    }

    @Override
    protected EventUserDto createDto() {
        return factory.manufacturePojo(EventUserDto.class);
    }
}
