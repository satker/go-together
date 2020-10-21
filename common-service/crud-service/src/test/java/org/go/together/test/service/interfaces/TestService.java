package org.go.together.test.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.find.FindService;
import org.go.together.test.dto.TestDto;
import org.go.together.test.entities.TestEntity;

import java.util.Collection;

public interface TestService extends CrudService<TestDto>, FindService<TestEntity> {
    void setAnotherClient(Collection<Object> result);
}
