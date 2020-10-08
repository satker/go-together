package org.go.together.test.repository.impl;

import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.test.entities.TestEntity;
import org.go.together.test.repository.interfaces.TestRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepositoryImpl extends CustomRepositoryImpl<TestEntity> implements TestRepository {
}
