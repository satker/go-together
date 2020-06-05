package org.go.together.test.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.test.entities.TestEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepository extends CustomRepository<TestEntity> {
}
