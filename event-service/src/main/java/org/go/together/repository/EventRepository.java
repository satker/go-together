package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.Event;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class EventRepository extends CustomRepository<Event> {
    public Collection<Event> findEventsByNameLike(String name, int start, int end) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE, name.toLowerCase()))
                .fetchAllPageable(start, end);
    }
}
