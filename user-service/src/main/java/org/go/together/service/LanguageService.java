package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.LanguageDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.Language;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class LanguageService extends CrudServiceImpl<LanguageDto, Language> {
    @Override
    public String getServiceName() {
        return "language";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("id", FieldMapper.builder()
                        .currentServiceField("id")
                        .fieldClass(UUID.class).build()).build();
    }
}
