package org.go.together.mapper;

import org.go.together.base.CommonMapper;
import org.go.together.dto.InterestDto;
import org.go.together.model.Interest;
import org.springframework.stereotype.Component;

@Component
public class InterestMapper extends CommonMapper<InterestDto, Interest> {
    @Override
    public InterestDto toDto(Interest entity) {
        InterestDto interestDto = new InterestDto();
        interestDto.setId(entity.getId());
        interestDto.setName(entity.getName());
        return interestDto;
    }

    @Override
    public Interest toEntity(InterestDto dto) {
        Interest interestRecord = new Interest();
        interestRecord.setId(dto.getId());
        interestRecord.setName(dto.getName());
        return interestRecord;
    }
}
