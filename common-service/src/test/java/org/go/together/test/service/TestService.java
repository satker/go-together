package org.go.together.test.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.CrudService;
import org.go.together.test.client.AnotherClient;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.mapper.TestMapper;
import org.go.together.test.repository.TestRepository;
import org.go.together.test.validation.TestValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class TestService extends CrudService<TestDto, TestEntity> {
    private final AnotherClient anotherClient;

    protected TestService(TestRepository repository,
                          TestMapper mapper,
                          TestValidator validator,
                          AnotherClient anotherClient) {
        super(repository, mapper, validator);
        this.anotherClient = anotherClient;
    }

    public void setAnotherClient(Collection<Object> result) {
        ResponseDto<Object> objectResponseDto = new ResponseDto<>();
        objectResponseDto.setPage(null);
        objectResponseDto.setResult(result);
        anotherClient.setResponseDto(objectResponseDto);
    }

    @Override
    public String getServiceName() {
        return "test";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("name", FieldMapper.builder()
                        .currentServiceField("name").build())
                .put("manyJoinEntities", FieldMapper.builder()
                        .currentServiceField("manyJoinEntities").build())
                .put("elements", FieldMapper.builder()
                        .currentServiceField("elements")
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("element")
                        .remoteServiceFieldGetter("id").build())
                .put("joinTestEntities", FieldMapper.builder()
                        .currentServiceField("joinTestEntities")
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("join")
                        .remoteServiceFieldGetter("id").build())
                .build();
    }
}
