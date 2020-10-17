package org.go.together.test.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.find.dto.FieldMapper;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class JoinTestService extends CrudServiceImpl<JoinTestDto, JoinTestEntity> {
    @Override
    public String getServiceName() {
        return "joinTest";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("id", FieldMapper.builder()
                        .currentServiceField("id")
                        .fieldClass(UUID.class).build(),
                "name", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build());
    }
}
