package org.go.together.service;

import org.go.together.dto.LanguageDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.LanguageMapper;
import org.go.together.repository.LanguageRepository;
import org.go.together.repository.tables.records.LanguageRecord;
import org.go.together.validation.LanguageValidator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LanguageService extends CrudService<LanguageDto, LanguageRecord, LanguageRepository> {
    private final LanguageMapper languageMapper;

    protected LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper,
                              LanguageValidator languageValidator) {
        super(languageRepository, languageMapper, languageValidator);
        this.languageMapper = languageMapper;
    }

    public Set<LanguageDto> getLanguages() {
        return /*super.findAll().values().stream()
                .map(languageMapper::entityToDto)
                .collect(Collectors.toSet())*/ null;

    }

    public boolean existsById(String id) {
        return /*super.findAll().get(id) != null*/ false;
    }

/*@Override
    public ImmutableMap<String, FunctionToGetValue> getFields() {
        return null;
    }

    @Override
    public String getServiceName() {
        return "language";
    }*/

}
