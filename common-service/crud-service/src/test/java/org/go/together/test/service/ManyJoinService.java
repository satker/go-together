package org.go.together.test.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.find.dto.FieldMapper;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.entities.ManyJoinEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ManyJoinService extends CrudServiceImpl<ManyJoinDto, ManyJoinEntity> {
    @Override
    public String getServiceName() {
        return "manyJoin";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("id", FieldMapper.builder()
                        .currentServiceField("id")
                        .fieldClass(UUID.class).build())
                .put("namew", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build())
                .put("idw", FieldMapper.builder()
                        .currentServiceField("id")
                        .fieldClass(UUID.class).build())
                .put("number", FieldMapper.builder()
                        .currentServiceField("number")
                        .fieldClass(Number.class).build())
                .build();
    }
}
