package org.go.together.test.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.test.dto.ComplexInnerDto;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.ComplexInnerEntity;
import org.go.together.test.entities.JoinTestEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JoinTestMapper implements Mapper<JoinTestDto, JoinTestEntity> {
    private final Mapper<ComplexInnerDto, ComplexInnerEntity> complexInnerMapper;

    @Override
    public JoinTestDto entityToDto(UUID requestId, JoinTestEntity entity) {
        JoinTestDto joinTestDto = new JoinTestDto();
        joinTestDto.setId(entity.getId());
        joinTestDto.setName(entity.getName());
        joinTestDto.setComplexInner(complexInnerMapper.entityToDto(requestId, entity.getComplexInner()));
        return joinTestDto;
    }

    @Override
    public JoinTestEntity dtoToEntity(JoinTestDto dto) {
        JoinTestEntity joinTestEntity = new JoinTestEntity();
        joinTestEntity.setId(dto.getId());
        joinTestEntity.setName(dto.getName());
        joinTestEntity.setComplexInner(complexInnerMapper.dtoToEntity(dto.getComplexInner()));
        return joinTestEntity;
    }
}
