package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.find.FindService;
import org.go.together.model.GroupPhoto;

public interface GroupPhotoService extends CrudService<GroupPhotoDto>, FindService<GroupPhoto> {
}
