package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.InterestDto;
import org.go.together.model.Interest;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryContext.class)
class InterestServiceTest extends CrudServiceCommonTest<Interest, InterestDto> {

    @Override
    protected InterestDto createDto() {
        return factory.manufacturePojo(InterestDto.class);
    }
}