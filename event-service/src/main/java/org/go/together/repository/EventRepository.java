package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.Event;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public class EventRepository extends CustomRepository<Event> {
    @Transactional
    public Collection<Event> findEventsByNameLike(String name, int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE, name.toLowerCase()))
                .fetchWithPageable(start, pageSize);
    }
}
