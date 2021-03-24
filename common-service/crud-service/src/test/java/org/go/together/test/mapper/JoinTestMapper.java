package org.go.together.test.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.test.dto.ComplexInnerDto;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.ComplexInnerEntity;
import org.go.together.test.entities.JoinTestEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JoinTestMapper extends CommonMapper<JoinTestDto, JoinTestEntity> {
    private final Mapper<ComplexInnerDto, ComplexInnerEntity> complexInnerMapper;

    @Override
    public JoinTestDto toDto(JoinTestEntity entity) {
        JoinTestDto joinTestDto = new JoinTestDto();
        joinTestDto.setId(entity.getId());
        joinTestDto.setName(entity.getName());
        joinTestDto.setComplexInner(complexInnerMapper.entityToDto(entity.getComplexInner()));
        return joinTestDto;
    }

    @Override
    public JoinTestEntity toEntity(JoinTestDto dto) {
        JoinTestEntity joinTestEntity = new JoinTestEntity();
        joinTestEntity.setId(dto.getId());
        joinTestEntity.setName(dto.getName());
        joinTestEntity.setComplexInner(complexInnerMapper.dtoToEntity(dto.getComplexInner()));
        return joinTestEntity;
    }
}
