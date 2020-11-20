package org.go.together.repository.impl;

import org.go.together.model.Event;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.EventRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryImpl extends CustomRepositoryImpl<Event> implements EventRepository {
}
