package org.go.together.service;

import org.go.together.dto.LanguageDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.CrudService;
import org.go.together.mapper.LanguageMapper;
import org.go.together.model.Language;
import org.go.together.repository.LanguageRepository;
import org.go.together.validation.LanguageValidator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LanguageService extends CrudService<LanguageDto, Language> {
    private final LanguageMapper languageMapper;
    private final LanguageRepository languageRepository;

    protected LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper,
                              LanguageValidator languageValidator) {
        super(languageRepository, languageMapper, languageValidator);
        this.languageMapper = languageMapper;
        this.languageRepository = languageRepository;
    }

    public Set<LanguageDto> getLanguages() {
        return languageRepository.findAll().stream()
                .map(languageMapper::entityToDto)
                .collect(Collectors.toSet());

    }

    @Override
    public String getServiceName() {
        return "language";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
