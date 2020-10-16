package org.go.together.repository.interfaces;

import org.go.together.model.Event;
import org.go.together.repository.CustomRepository;

import java.util.Collection;

public interface EventRepository extends CustomRepository<Event> {
    Collection<Event> findEventsByNameLike(String name, int start, int pageSize);
}
