package org.go.together.repository.impl;

import org.go.together.model.Photo;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.PhotoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PhotoRepositoryImpl extends CustomRepositoryImpl<Photo> implements PhotoRepository {
}
