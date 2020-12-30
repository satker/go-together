package org.go.together.test.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.ResponseDto;
import org.go.together.test.client.AnotherClient;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.service.interfaces.JoinTestService;
import org.go.together.test.service.interfaces.ManyJoinService;
import org.go.together.test.service.interfaces.TestService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
public class TestServiceImpl extends CommonCrudService<TestDto, TestEntity> implements TestService {
    private final AnotherClient anotherClient;
    private final ManyJoinService manyJoinService;
    private final JoinTestService joinTestService;

    protected TestServiceImpl(AnotherClient anotherClient,
                              ManyJoinService manyJoinService,
                              JoinTestService joinTestService) {
        this.anotherClient = anotherClient;
        this.manyJoinService = manyJoinService;
        this.joinTestService = joinTestService;
    }

    @Override
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
        return Map.of("name", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build(),
                "number", FieldMapper.builder()
                        .currentServiceField("number")
                        .fieldClass(Number.class).build(),
                "names", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build(),
                "numbers", FieldMapper.builder()
                        .currentServiceField("number")
                        .fieldClass(Number.class).build(),
                "manyJoinEntities", FieldMapper.builder()
                        .innerService(manyJoinService)
                        .currentServiceField("manyJoinEntities").build(),
                "elements", FieldMapper.builder()
                        .currentServiceField("elements")
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("element")
                        .remoteServiceFieldGetter("id")
                        .fieldClass(UUID.class).build(),
                "joinTestEntities", FieldMapper.builder()
                        .currentServiceField("joinTestEntities")
                        .innerService(joinTestService)
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("join")
                        .remoteServiceFieldGetter("id")
                        .fieldClass(UUID.class).build(),
                "joinTestEntitiesInner", FieldMapper.builder()
                        .currentServiceField("joinTestEntities")
                        .innerService(joinTestService)
                        .fieldClass(UUID.class).build(),
                "elementss", FieldMapper.builder()
                        .currentServiceField("elements")
                        .remoteServiceClient(anotherClient)
                        .remoteServiceName("element")
                        .remoteServiceFieldGetter("id")
                        .fieldClass(UUID.class).build());
    }
}
