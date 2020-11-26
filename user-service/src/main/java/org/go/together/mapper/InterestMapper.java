package org.go.together.mapper;

import org.go.together.base.Mapper;
import org.go.together.dto.InterestDto;
import org.go.together.model.Interest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InterestMapper implements Mapper<InterestDto, Interest> {
    @Override
    public InterestDto entityToDto(UUID requestId, Interest entity) {
        InterestDto interestDto = new InterestDto();
        interestDto.setId(entity.getId());
        interestDto.setName(entity.getName());
        return interestDto;
    }

    @Override
    public Interest dtoToEntity(InterestDto dto) {
        Interest interestRecord = new Interest();
        interestRecord.setId(dto.getId());
        interestRecord.setName(dto.getName());
        return interestRecord;
    }
}
