package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.GroupRouteInfo;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = RepositoryContext.class)
public class GroupRouteInfoTest extends CrudServiceCommonTest<GroupRouteInfo, GroupRouteInfoDto> {
    @Override
    protected GroupRouteInfoDto createDto() {
        return factory.manufacturePojo(GroupRouteInfoDto.class);
    }

    @Override
    protected void checkDtos(GroupRouteInfoDto dto, GroupRouteInfoDto savedObject, CrudOperation operation) {
        if (operation == CrudOperation.CREATE) {
            assertEquals(dto.getGroupId(), savedObject.getGroupId());
            assertEquals(dto.getInfoRoutes().size(), dto.getInfoRoutes().size());
        } else if (operation == CrudOperation.UPDATE) {
            assertEquals(dto.getInfoRoutes().size(), dto.getInfoRoutes().size());
        }
    }
}
