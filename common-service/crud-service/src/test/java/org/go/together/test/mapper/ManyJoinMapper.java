package org.go.together.test.mapper;

import org.go.together.base.CommonMapper;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.entities.ManyJoinEntity;
import org.springframework.stereotype.Component;

@Component
public class ManyJoinMapper extends CommonMapper<ManyJoinDto, ManyJoinEntity> {
    @Override
    public ManyJoinDto toDto(ManyJoinEntity entity) {
        ManyJoinDto manyJoinDto = new ManyJoinDto();
        manyJoinDto.setId(entity.getId());
        manyJoinDto.setName(entity.getName());
        manyJoinDto.setNumber(entity.getNumber());
        return manyJoinDto;
    }

    @Override
    public ManyJoinEntity toEntity(ManyJoinDto dto) {
        ManyJoinEntity manyJoinEntity = new ManyJoinEntity();
        manyJoinEntity.setId(dto.getId());
        manyJoinEntity.setName(dto.getName());
        manyJoinEntity.setNumber(dto.getNumber());
        return manyJoinEntity;
    }
}
