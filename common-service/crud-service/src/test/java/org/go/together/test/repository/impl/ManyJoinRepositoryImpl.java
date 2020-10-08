package org.go.together.test.repository.impl;

import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.test.entities.ManyJoinEntity;
import org.go.together.test.repository.interfaces.ManyJoinRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ManyJoinRepositoryImpl extends CustomRepositoryImpl<ManyJoinEntity> implements ManyJoinRepository {
}
