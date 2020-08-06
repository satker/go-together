package org.go.together.test.repository;

import org.go.together.repository.CustomRepository;
import org.go.together.test.entities.ManyJoinEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ManyJoinRepository extends CustomRepository<ManyJoinEntity> {
}
