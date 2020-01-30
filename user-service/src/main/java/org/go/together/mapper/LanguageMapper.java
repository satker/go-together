package org.go.together.mapper;

import org.go.together.dto.LanguageDto;
import org.go.together.interfaces.Mapper;
import org.go.together.repository.tables.records.LanguageRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LanguageMapper implements Mapper<LanguageDto, LanguageRecord> {
    @Override
    public LanguageDto entityToDto(LanguageRecord entity) {
        LanguageDto languageDto = new LanguageDto();

        languageDto.setId(UUID.fromString(entity.getId()));
        languageDto.setName(entity.getName());
        languageDto.setCode(entity.getCode());
        return languageDto;
    }

    @Override
    public LanguageRecord dtoToEntity(LanguageDto dto) {
        LanguageRecord language = new LanguageRecord();
        language.setId(dto.getId().toString());
        language.setName(dto.getName());
        language.setCode(dto.getCode());
        return language;
    }
}
