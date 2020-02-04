package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.model.Language;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;


@Repository
public class LanguageRepository extends CustomRepository<Language> {
    protected LanguageRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
