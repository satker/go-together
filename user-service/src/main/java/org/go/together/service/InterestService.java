package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.InterestDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.InterestMapper;
import org.go.together.model.Interest;
import org.go.together.repository.InterestRepository;
import org.go.together.validation.InterestValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InterestService extends CrudService<InterestDto, Interest> {
    protected InterestService(InterestRepository interestRepository,
                              InterestMapper interestMapper,
                              InterestValidator interestValidator) {
        super(interestRepository, interestMapper, interestValidator);
    }

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
