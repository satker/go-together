package org.go.together.test.repository.impl;

import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.test.entities.ComplexInnerEntity;
import org.go.together.test.repository.interfaces.ComplexInnerRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ComplexInnerRepositoryImpl extends CustomRepositoryImpl<ComplexInnerEntity> implements ComplexInnerRepository {
}
