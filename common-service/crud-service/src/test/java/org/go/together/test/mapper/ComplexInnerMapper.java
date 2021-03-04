package org.go.together.test.mapper;

import org.go.together.base.Mapper;
import org.go.together.test.dto.ComplexInnerDto;
import org.go.together.test.entities.ComplexInnerEntity;
import org.springframework.stereotype.Component;

@Component
public class ComplexInnerMapper implements Mapper<ComplexInnerDto, ComplexInnerEntity> {
    @Override
    public ComplexInnerDto entityToDto(ComplexInnerEntity entity) {
        ComplexInnerDto complexInnerDto = new ComplexInnerDto();
        complexInnerDto.setId(entity.getId());
        complexInnerDto.setName(entity.getName());
        return complexInnerDto;
    }

    @Override
    public ComplexInnerEntity dtoToEntity(ComplexInnerDto dto) {
        ComplexInnerEntity complexInnerEntity = new ComplexInnerEntity();
        complexInnerEntity.setId(dto.getId());
        complexInnerEntity.setName(dto.getName());
        return complexInnerEntity;
    }
}
