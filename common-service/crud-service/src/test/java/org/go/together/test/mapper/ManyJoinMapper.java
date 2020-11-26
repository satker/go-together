package org.go.together.test.mapper;

import org.go.together.base.Mapper;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.entities.ManyJoinEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ManyJoinMapper implements Mapper<ManyJoinDto, ManyJoinEntity> {
    @Override
    public ManyJoinDto entityToDto(UUID requestId, ManyJoinEntity entity) {
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
