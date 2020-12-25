package org.go.together.test.mapper;

import org.go.together.base.Mapper;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JoinTestMapper implements Mapper<JoinTestDto, JoinTestEntity> {
    @Override
    public JoinTestDto entityToDto(UUID requestId, JoinTestEntity entity) {
        JoinTestDto joinTestDto = new JoinTestDto();
        joinTestDto.setId(entity.getId());
        joinTestDto.setName(entity.getName());
        return joinTestDto;
    }

    @Override
    public JoinTestEntity dtoToEntity(JoinTestDto dto) {
        JoinTestEntity joinTestEntity = new JoinTestEntity();
        joinTestEntity.setId(dto.getId());
        joinTestEntity.setName(dto.getName());
        return joinTestEntity;
    }
}
