package org.go.together.test.service.impl;

import org.go.together.base.impl.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.service.interfaces.ManyJoinService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ManyJoinServiceImpl extends CommonCrudService<ManyJoinDto, ManyJoinEntity> implements ManyJoinService {
    @Override
    public String getServiceName() {
        return "manyJoin";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("id", FieldMapper.builder()
                        .currentServiceField("id")
                        .fieldClass(UUID.class).build(),
                "namew", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build(),
                "idw", FieldMapper.builder()
                        .currentServiceField("id")
                        .fieldClass(UUID.class).build(),
                "number", FieldMapper.builder()
                        .currentServiceField("number")
                        .fieldClass(Number.class).build());
    }
}
