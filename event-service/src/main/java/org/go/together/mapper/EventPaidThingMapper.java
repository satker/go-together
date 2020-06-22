package org.go.together.mapper;

import org.go.together.dto.EventPaidThingDto;
import org.go.together.logic.Mapper;
import org.go.together.model.EventPaidThing;
import org.springframework.stereotype.Component;

@Component
public class EventPaidThingMapper implements Mapper<EventPaidThingDto, EventPaidThing> {
    private final PaidThingMapper paidThingMapper;

    public EventPaidThingMapper(PaidThingMapper paidThingMapper) {
        this.paidThingMapper = paidThingMapper;
    }

    @Override
    public EventPaidThingDto entityToDto(EventPaidThing entity) {
        EventPaidThingDto eventPaidThingDto = new EventPaidThingDto();
        eventPaidThingDto.setCashCategory(entity.getCashCategory());
        eventPaidThingDto.setId(entity.getId());
        eventPaidThingDto.setPaidThing(paidThingMapper.entityToDto(entity.getPaidThing()));
        return eventPaidThingDto;
    }

    @Override
    public EventPaidThing dtoToEntity(EventPaidThingDto dto) {
        EventPaidThing eventPaidThing = new EventPaidThing();
        eventPaidThing.setId(dto.getId());
        eventPaidThing.setCashCategory(dto.getCashCategory());
        eventPaidThing.setPaidThing(paidThingMapper.dtoToEntity(dto.getPaidThing()));
        return eventPaidThing;
    }
}
