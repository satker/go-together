package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.CrudServiceImpl;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.InterestDto;
import org.go.together.model.Interest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InterestService extends CrudServiceImpl<InterestDto, Interest> {
    @Override
    public String getServiceName() {
        return "interest";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("id", FieldMapper.builder()
                        .currentServiceField("id").build()).build();
    }
}
