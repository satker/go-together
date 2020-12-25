package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.RouteInfoDto;
import org.go.together.model.RouteInfo;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryContext.class)
public class RouteInfoServiceTest extends CrudServiceCommonTest<RouteInfo, RouteInfoDto> {

    @Override
    protected RouteInfoDto createDto() {
        return factory.manufacturePojo(RouteInfoDto.class);
    }
}