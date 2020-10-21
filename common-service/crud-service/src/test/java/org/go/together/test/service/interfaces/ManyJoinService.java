package org.go.together.test.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.find.FindService;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.entities.ManyJoinEntity;

public interface ManyJoinService extends CrudService<ManyJoinDto>, FindService<ManyJoinEntity> {
}
