package org.go.together.test.mapper;

import org.go.together.mapper.Mapper;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.entities.ManyJoinEntity;
import org.springframework.stereotype.Component;

@Component
public class ManyJoinMapper implements Mapper<ManyJoinDto, ManyJoinEntity> {
    @Override
    public ManyJoinDto entityToDto(ManyJoinEntity entity) {
        ManyJoinDto manyJoinDto = new ManyJoinDto();
        manyJoinDto.setId(entity.getId());
        manyJoinDto.setName(entity.getName());
        manyJoinDto.setNumber(entity.getNumber());
        return manyJoinDto;
    }

    @Override
    public ManyJoinEntity dtoToEntity(ManyJoinDto dto) {
        ManyJoinEntity manyJoinEntity = new ManyJoinEntity();
        manyJoinEntity.setId(dto.getId());
        manyJoinEntity.setName(dto.getName());
        manyJoinEntity.setNumber(dto.getNumber());
        return manyJoinEntity;
    }
}
