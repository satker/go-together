package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.EventPaidThingDto;
import org.go.together.dto.PaidThingDto;
import org.go.together.mapper.Mapper;
import org.go.together.model.EventPaidThing;
import org.go.together.model.PaidThing;
import org.go.together.notification.streams.NotificationSource;
import org.go.together.repository.interfaces.PaidThingRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
class EventPaidThingServiceTest extends CrudServiceCommonTest<EventPaidThing, EventPaidThingDto> {
    @Autowired
    private PaidThingRepository paidThingRepository;

    @Autowired
    private Mapper<PaidThingDto, PaidThing> paidThingMapper;

    @Autowired
    private NotificationSource source;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        MessageChannel messageChannel = Mockito.mock(MessageChannel.class);
        when(source.output()).thenReturn(messageChannel);
        when(messageChannel.send(any())).thenReturn(true);
    }

    @Override
    protected EventPaidThingDto createDto() {
        EventPaidThingDto eventPaidThingDto = factory.manufacturePojo(EventPaidThingDto.class);
        PaidThing paidThing = paidThingMapper.dtoToEntity(eventPaidThingDto.getPaidThing());
        PaidThing savedPaidThing = paidThingRepository.save(paidThing);
        eventPaidThingDto.setPaidThing(paidThingMapper.entityToDto(savedPaidThing));
        return eventPaidThingDto;
    }
}