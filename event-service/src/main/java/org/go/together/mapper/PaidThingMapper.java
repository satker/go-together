package org.go.together.mapper;

import org.go.together.dto.PaidThingDto;
import org.go.together.logic.Mapper;
import org.go.together.model.PaidThing;
import org.springframework.stereotype.Component;

@Component
public class PaidThingMapper implements Mapper<PaidThingDto, PaidThing> {
    @Override
    public PaidThingDto entityToDto(PaidThing entity) {
        PaidThingDto paidThingDto = new PaidThingDto();
        paidThingDto.setId(entity.getId());
        paidThingDto.setName(entity.getName());
        return paidThingDto;
    }

    @Override
    public PaidThing dtoToEntity(PaidThingDto dto) {
        PaidThing paidThing = new PaidThing();
        paidThing.setId(dto.getId());
        paidThing.setName(dto.getName());
        return paidThing;
    }
}
