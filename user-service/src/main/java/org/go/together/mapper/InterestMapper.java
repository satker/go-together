package org.go.together.mapper;

import org.go.together.dto.InterestDto;
import org.go.together.interfaces.Mapper;
import org.go.together.repository.tables.records.InterestRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InterestMapper implements Mapper<InterestDto, InterestRecord> {
    @Override
    public InterestDto entityToDto(InterestRecord entity) {
        InterestDto interestDto = new InterestDto();
        interestDto.setId(UUID.fromString(entity.getId()));
        interestDto.setName(entity.getName());
        return interestDto;
    }

    @Override
    public InterestRecord dtoToEntity(InterestDto dto) {
        InterestRecord interestRecord = new InterestRecord();
        interestRecord.setId(dto.getId().toString());
        interestRecord.setName(dto.getName());
        return interestRecord;
    }
}
