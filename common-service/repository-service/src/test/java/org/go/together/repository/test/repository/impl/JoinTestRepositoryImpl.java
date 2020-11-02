package org.go.together.repository.test.repository.impl;

import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.test.entities.JoinTestEntity;
import org.go.together.repository.test.repository.interfaces.JoinTestRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JoinTestRepositoryImpl extends CustomRepositoryImpl<JoinTestEntity> implements JoinTestRepository {
}
