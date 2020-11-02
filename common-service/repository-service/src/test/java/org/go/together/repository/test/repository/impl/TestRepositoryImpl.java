package org.go.together.repository.test.repository.impl;

import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.test.entities.TestEntity;
import org.go.together.repository.test.repository.interfaces.TestRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepositoryImpl extends CustomRepositoryImpl<TestEntity> implements TestRepository {
}
