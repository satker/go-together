package org.go.together.repository;

import org.go.together.model.Location;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Repository
public class LocationRepository extends CustomRepository<Location> {
}
