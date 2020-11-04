package org.go.together.test.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.test.dto.TestDto;

import java.util.Collection;

public interface TestService extends CrudService<TestDto>, FindService<TestDto> {
    void setAnotherClient(Collection<Object> result);
}
