package org.go.together.service.impl;

import org.go.together.base.impl.CommonCrudService;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.InterestDto;
import org.go.together.model.Interest;
import org.go.together.service.interfaces.InterestService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class InterestServiceImpl extends CommonCrudService<InterestDto, Interest> implements InterestService {
    @Override
    public String getServiceName() {
        return "interest";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("id", FieldMapper.builder()
                .currentServiceField("id")
                .fieldClass(UUID.class).build());
    }
}
