package org.go.together.test.mapper;

import org.go.together.base.Mapper;
import org.go.together.dto.SimpleDto;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.entities.TestEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TestMapper implements Mapper<TestDto, TestEntity> {
    private final Mapper<ManyJoinDto, ManyJoinEntity> manyJoinMapper;
    private final Mapper<JoinTestDto, JoinTestEntity> joinTestMapper;

    public TestMapper(Mapper<ManyJoinDto, ManyJoinEntity> manyJoinMapper,
                      Mapper<JoinTestDto, JoinTestEntity> joinTestMapper) {
        this.manyJoinMapper = manyJoinMapper;
        this.joinTestMapper = joinTestMapper;
    }

    @Override
    public TestDto entityToDto(TestEntity entity) {
        TestDto testDto = new TestDto();
        testDto.setId(entity.getId());
        testDto.setEndDate(entity.getEndDate());
        testDto.setStartDate(entity.getStartDate());
        testDto.setEndNumber(entity.getEndNumber());
        testDto.setStartNumber(entity.getStartNumber());
        testDto.setLatitude(entity.getLatitude());
        testDto.setLongitude(entity.getLongitude());
        testDto.setSimpleDto(new SimpleDto(entity.getSimpleDto(), entity.getSimpleDto()));
        testDto.setDate(entity.getDate());
        testDto.setName(entity.getName());
        testDto.setNumber(entity.getNumber());
        testDto.setElements(entity.getElements());
        testDto.setJoinTestEntities(entity.getJoinTestEntities().stream()
                .map(joinTest -> joinTestMapper.entityToDto(joinTest))
                .collect(Collectors.toSet()));
        testDto.setManyJoinEntities(entity.getManyJoinEntities().stream()
                .map(manyJoin -> manyJoinMapper.entityToDto(manyJoin))
                .collect(Collectors.toSet()));
        return testDto;
    }

    @Override
    public TestEntity dtoToEntity(TestDto dto) {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(dto.getId());
        testEntity.setDate(dto.getDate());
        testEntity.setName(dto.getName());
        testEntity.setNumber(dto.getNumber());
        testEntity.setElements(dto.getElements());
        testEntity.setJoinTestEntities(dto.getJoinTestEntities().stream().map(joinTestMapper::dtoToEntity).collect(Collectors.toSet()));
        testEntity.setManyJoinEntities(dto.getManyJoinEntities().stream().map(manyJoinMapper::dtoToEntity).collect(Collectors.toSet()));
        testEntity.setEndDate(dto.getEndDate());
        testEntity.setEndNumber(dto.getEndNumber());
        testEntity.setLatitude(dto.getLatitude());
        testEntity.setLongitude(dto.getLongitude());
        testEntity.setStartDate(dto.getStartDate());
        testEntity.setStartNumber(dto.getStartNumber());
        testEntity.setSimpleDto(dto.getSimpleDto().getId());
        return testEntity;
    }
}
