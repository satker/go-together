package org.go.together.test.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.CrudService;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.mapper.ManyJoinMapper;
import org.go.together.test.repository.ManyJoinRepository;
import org.go.together.test.validation.ManyJoinValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ManyJoinService extends CrudService<ManyJoinDto, ManyJoinEntity> {
    protected ManyJoinService(ManyJoinRepository repository,
                              ManyJoinMapper mapper,
                              ManyJoinValidator validator) {
        super(repository, mapper, validator);
    }

    @Override
    public String getServiceName() {
        return "manyJoin";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("id", FieldMapper.builder()
                        .currentServiceField("id").build())
                .put("namew", FieldMapper.builder()
                        .currentServiceField("name").build())
                .put("idw", FieldMapper.builder()
                        .currentServiceField("id").build())
                .put("number", FieldMapper.builder()
                        .currentServiceField("name").build())
                .build();
    }
}
