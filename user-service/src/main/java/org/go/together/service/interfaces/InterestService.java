package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.InterestDto;
import org.go.together.find.FindService;
import org.go.together.model.Interest;

public interface InterestService extends CrudService<InterestDto>, FindService<Interest> {
}
