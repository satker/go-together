package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.Event;

import java.util.Collection;

public interface EventRepository extends CustomRepository<Event> {
    Collection<Event> findEventsByNameLike(String name, int start, int pageSize);
}
