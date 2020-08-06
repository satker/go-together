package org.go.together.test.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.ResponseDto;
import org.go.together.test.client.AnotherClient;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class TestService extends CrudServiceImpl<TestDto, TestEntity> {
    private final AnotherClient anotherClient;
    private final ManyJoinService manyJoinService;
    private final JoinTestService joinTestService;

    protected TestService(AnotherClient anotherClient,
                          ManyJoinService manyJoinService,
                          JoinTestService joinTestService) {
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
