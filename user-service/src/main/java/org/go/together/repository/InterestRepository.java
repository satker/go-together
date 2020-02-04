package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.model.Interest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;

@Repository
public class InterestRepository extends CustomRepository<Interest> {
    protected InterestRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
