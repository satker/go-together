package org.go.together.validation;

import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

@Component
public class GroupRouteInfoValidator extends CommonValidator<GroupRouteInfoDto> {
    @Override
    public void getMapsForCheck(GroupRouteInfoDto dto) {

    }
}
