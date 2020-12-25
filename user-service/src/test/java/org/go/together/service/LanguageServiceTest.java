package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.LanguageDto;
import org.go.together.model.Language;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryContext.class)
class LanguageServiceTest extends CrudServiceCommonTest<Language, LanguageDto> {
    @Override
    @BeforeEach
    public void init() {
        super.init();
    }

    @Override
    protected LanguageDto createDto() {
        return factory.manufacturePojo(LanguageDto.class);
    }
}