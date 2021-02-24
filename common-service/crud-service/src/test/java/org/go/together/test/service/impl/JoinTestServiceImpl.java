package org.go.together.test.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.service.interfaces.ComplexInnerService;
import org.go.together.test.service.interfaces.JoinTestService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JoinTestServiceImpl extends CommonCrudService<JoinTestDto, JoinTestEntity> implements JoinTestService {
    private final ComplexInnerService complexInnerService;

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
                        .fieldClass(String.class).build(),
                "complexInner", FieldMapper.builder()
                        .innerService(complexInnerService)
                        .currentServiceField("complexInner").build());
    }
}
