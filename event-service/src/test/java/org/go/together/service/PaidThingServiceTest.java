package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.PaidThingDto;
import org.go.together.model.PaidThing;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryContext.class)
public class PaidThingServiceTest extends CrudServiceCommonTest<PaidThing, PaidThingDto> {
    @Override
    protected PaidThingDto createDto() {
        return factory.manufacturePojo(PaidThingDto.class);
    }
}
