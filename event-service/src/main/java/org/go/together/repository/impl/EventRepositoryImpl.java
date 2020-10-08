package org.go.together.repository.impl;

import org.go.together.model.Event;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.EventRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class EventRepositoryImpl extends CustomRepositoryImpl<Event> implements EventRepository {
    @Override
    public Collection<Event> findEventsByNameLike(String name, int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE, name))
                .fetchWithPageable(start, pageSize);
    }
}
