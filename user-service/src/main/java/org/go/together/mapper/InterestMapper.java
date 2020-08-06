package org.go.together.mapper;

import org.go.together.model.Interest;
import org.go.together.notification.dto.InterestDto;
import org.springframework.stereotype.Component;

@Component
public class InterestMapper implements Mapper<InterestDto, Interest> {
    @Override
    public InterestDto entityToDto(Interest entity) {
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
