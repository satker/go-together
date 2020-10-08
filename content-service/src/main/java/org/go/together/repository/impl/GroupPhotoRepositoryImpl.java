package org.go.together.repository.impl;

import org.go.together.model.GroupPhoto;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.GroupPhotoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class GroupPhotoRepositoryImpl extends CustomRepositoryImpl<GroupPhoto> implements GroupPhotoRepository {
}
