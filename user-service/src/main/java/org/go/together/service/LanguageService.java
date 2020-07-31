package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.CrudServiceImpl;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.LanguageDto;
import org.go.together.mapper.LanguageMapper;
import org.go.together.model.Language;
import org.go.together.repository.LanguageRepository;
import org.go.together.validation.LanguageValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LanguageService extends CrudServiceImpl<LanguageDto, Language> {
    protected LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper,
                              LanguageValidator languageValidator) {
        super(languageRepository, languageMapper, languageValidator);
    }

    @Override
    public String getServiceName() {
        return "language";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("id", FieldMapper.builder()
                        .currentServiceField("id").build()).build();
    }
}
