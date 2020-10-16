package org.go.together.repository.impl;

import org.go.together.model.EventPaidThing;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.EventPaidThingRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EventPaidThingRepositoryImpl extends CustomRepositoryImpl<EventPaidThing>
        implements EventPaidThingRepository {
}
