package org.go.together.mapper;

import org.go.together.dto.LanguageDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper implements Mapper<LanguageDto, Language> {
    @Override
    public LanguageDto entityToDto(Language entity) {
        LanguageDto languageDto = new LanguageDto();

        languageDto.setId(entity.getId());
        languageDto.setName(entity.getName());
        languageDto.setCode(entity.getCode());
        return languageDto;
    }

    @Override
    public Language dtoToEntity(LanguageDto dto) {
        Language language = new Language();
        language.setId(dto.getId());
        language.setName(dto.getName());
        language.setCode(dto.getCode());
        return language;
    }
}
