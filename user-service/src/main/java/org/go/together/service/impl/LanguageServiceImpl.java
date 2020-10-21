package org.go.together.service.impl;

import org.go.together.base.impl.CommonCrudService;
import org.go.together.dto.LanguageDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.Language;
import org.go.together.service.interfaces.LanguageService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class LanguageServiceImpl extends CommonCrudService<LanguageDto, Language> implements LanguageService {
    @Override
    public String getServiceName() {
        return "language";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("id", FieldMapper.builder()
                .currentServiceField("id")
                .fieldClass(UUID.class).build());
    }
}
