package org.go.together.test.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.test.dto.ComplexInnerDto;
import org.go.together.test.entities.ComplexInnerEntity;
import org.go.together.test.service.interfaces.ComplexInnerService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ComplexInnerServiceImpl extends CommonCrudService<ComplexInnerDto, ComplexInnerEntity> implements ComplexInnerService {
    @Override
    public String getServiceName() {
        return "complexInnerEntity";
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
