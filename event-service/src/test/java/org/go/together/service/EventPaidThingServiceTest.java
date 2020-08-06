package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.mapper.PaidThingMapper;
import org.go.together.model.EventPaidThing;
import org.go.together.model.PaidThing;
import org.go.together.notification.dto.EventPaidThingDto;
import org.go.together.notification.repository.PaidThingRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryContext.class)
class EventPaidThingServiceTest extends CrudServiceCommonTest<EventPaidThing, EventPaidThingDto> {
    @Autowired
    private PaidThingRepository paidThingRepository;

    @Autowired
    private PaidThingMapper paidThingMapper;

    @Override
    protected EventPaidThingDto createDto() {
        EventPaidThingDto eventPaidThingDto = factory.manufacturePojo(EventPaidThingDto.class);
        PaidThing paidThing = paidThingMapper.dtoToEntity(eventPaidThingDto.getPaidThing());
        PaidThing savedPaidThing = paidThingRepository.save(paidThing);
        eventPaidThingDto.setPaidThing(paidThingMapper.entityToDto(savedPaidThing));
        return eventPaidThingDto;
    }
}