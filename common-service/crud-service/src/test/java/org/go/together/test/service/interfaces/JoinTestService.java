package org.go.together.test.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.find.FindService;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.entities.JoinTestEntity;

public interface JoinTestService extends CrudService<JoinTestDto>, FindService<JoinTestEntity> {
}
