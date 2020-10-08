package org.go.together.repository.impl;

import org.go.together.model.Interest;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.InterestRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InterestRepositoryImpl extends CustomRepositoryImpl<Interest> implements InterestRepository {
}
