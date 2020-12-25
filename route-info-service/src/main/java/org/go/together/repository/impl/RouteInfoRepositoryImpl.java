package org.go.together.repository.impl;

import org.go.together.model.RouteInfo;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.RouteInfoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RouteInfoRepositoryImpl extends CustomRepositoryImpl<RouteInfo>
        implements RouteInfoRepository {
}
