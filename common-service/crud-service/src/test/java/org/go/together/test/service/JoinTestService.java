package org.go.together.test.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.find.dto.FieldMapper;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JoinTestService extends CrudServiceImpl<JoinTestDto, JoinTestEntity> {
    @Override
    public String getServiceName() {
        return "joinTest";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("id", FieldMapper.builder()
                        .currentServiceField("id").build())
                .put("name", FieldMapper.builder()
                        .currentServiceField("name").build())
                .build();
    }
}
