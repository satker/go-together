package org.go.together.repository.impl;

import org.go.together.model.GroupLocation;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.GroupLocationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class GroupLocationRepositoryImpl extends CustomRepositoryImpl<GroupLocation> implements GroupLocationRepository {
}
