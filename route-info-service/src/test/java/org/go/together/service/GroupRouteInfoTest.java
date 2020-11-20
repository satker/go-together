package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.model.GroupRouteInfo;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryContext.class)
public class GroupRouteInfoTest extends CrudServiceCommonTest<GroupRouteInfo, GroupRouteInfoDto> {
    @Override
    protected GroupRouteInfoDto createDto() {
        return factory.manufacturePojo(GroupRouteInfoDto.class);
    }
}
