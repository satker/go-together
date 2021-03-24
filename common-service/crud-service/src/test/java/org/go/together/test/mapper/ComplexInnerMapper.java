package org.go.together.test.mapper;

import org.go.together.base.CommonMapper;
import org.go.together.test.dto.ComplexInnerDto;
import org.go.together.test.entities.ComplexInnerEntity;
import org.springframework.stereotype.Component;

@Component
public class ComplexInnerMapper extends CommonMapper<ComplexInnerDto, ComplexInnerEntity> {
    @Override
    public ComplexInnerDto toDto(ComplexInnerEntity entity) {
        ComplexInnerDto complexInnerDto = new ComplexInnerDto();
        complexInnerDto.setId(entity.getId());
        complexInnerDto.setName(entity.getName());
        return complexInnerDto;
    }

    @Override
    public ComplexInnerEntity toEntity(ComplexInnerDto dto) {
        ComplexInnerEntity complexInnerEntity = new ComplexInnerEntity();
        complexInnerEntity.setId(dto.getId());
        complexInnerEntity.setName(dto.getName());
        return complexInnerEntity;
    }
}
