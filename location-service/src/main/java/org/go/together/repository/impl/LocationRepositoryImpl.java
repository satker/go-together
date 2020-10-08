package org.go.together.repository.impl;

import org.go.together.model.Location;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.LocationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepositoryImpl extends CustomRepositoryImpl<Location> implements LocationRepository {
}
