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
    private final ManyJoinService manyJoinService;
    private final JoinTestService joinTestService;

    protected TestService(TestRepository repository,
                          TestMapper mapper,
                          TestValidator validator,
                          AnotherClient anotherClient,
                          ManyJoinService manyJoinService,
                          JoinTestService joinTestService) {
        super(repository, mapper, validator);
        this.anotherClient = anotherClient;
        this.manyJoinService = manyJoinService;
        this.joinTestService = joinTestService;
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
                .put("number", FieldMapper.builder()
                        .currentServiceField("number").build())
                .put("names", FieldMapper.builder()
                        .currentServiceField("name").build())
                .put("numbers", FieldMapper.builder()
                        .currentServiceField("number").build())
                .put("manyJoinEntities", FieldMapper.builder()
                        .innerService(manyJoinService)
                        .currentServiceField("manyJoinEntities").build())
                .put("elements", FieldMapper.builder()
                        .currentServiceField("elements")
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("element")
                        .remoteServiceFieldGetter("id").build())
                .put("joinTestEntities", FieldMapper.builder()
                        .currentServiceField("joinTestEntities")
                        .innerService(joinTestService)
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("join")
                        .remoteServiceFieldGetter("id").build())
                .put("elementss", FieldMapper.builder()
                        .currentServiceField("elements")
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("element")
                        .remoteServiceFieldGetter("id").build())
                .build();
    }
}
