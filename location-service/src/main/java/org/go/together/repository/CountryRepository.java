package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.Country;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class CountryRepository extends CustomRepository<Country> {
    @Transactional
    public Optional<Country> findByName(String name) {
        return createQuery().where(createWhere().condition("name", SqlOperator.EQUAL, name.toUpperCase())).fetchOne();
    }
}
