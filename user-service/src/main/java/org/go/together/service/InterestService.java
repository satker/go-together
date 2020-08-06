package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.Interest;
import org.go.together.notification.dto.InterestDto;
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
