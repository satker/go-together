package org.go.together.test.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.CrudService;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.JoinTestEntity;
import org.go.together.test.mapper.JoinTestMapper;
import org.go.together.test.repository.JoinTestRepository;
import org.go.together.test.validation.JoinTestValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JoinTestService extends CrudService<JoinTestDto, JoinTestEntity> {
    protected JoinTestService(JoinTestRepository repository,
                              JoinTestMapper mapper,
                              JoinTestValidator validator) {
        super(repository, mapper, validator);
    }

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
