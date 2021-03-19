package org.go.together.mapper;

import org.go.together.base.CommonMapper;
import org.go.together.dto.LanguageDto;
import org.go.together.model.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper extends CommonMapper<LanguageDto, Language> {
    @Override
    public LanguageDto toDto(Language entity) {
        LanguageDto languageDto = new LanguageDto();

        languageDto.setId(entity.getId());
        languageDto.setName(entity.getName());
        languageDto.setCode(entity.getCode());
        return languageDto;
    }

    @Override
    public Language toEntity(LanguageDto dto) {
        Language language = new Language();
        language.setId(dto.getId());
        language.setName(dto.getName());
        language.setCode(dto.getCode());
        return language;
    }
}
