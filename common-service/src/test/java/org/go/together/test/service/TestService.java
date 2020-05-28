package org.go.together.test.service;

import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.CrudService;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.mapper.TestMapper;
import org.go.together.test.repository.TestRepository;
import org.go.together.test.validation.TestValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService extends CrudService<TestDto, TestEntity> {
    protected TestService(TestRepository repository,
                          TestMapper mapper,
                          TestValidator validator) {
        super(repository, mapper, validator);
    }

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
