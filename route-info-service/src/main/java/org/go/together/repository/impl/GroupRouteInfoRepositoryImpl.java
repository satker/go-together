package org.go.together.repository.impl;

import org.go.together.model.GroupRouteInfo;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.GroupRouteInfoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class GroupRouteInfoRepositoryImpl extends CustomRepositoryImpl<GroupRouteInfo>
        implements GroupRouteInfoRepository {
}
