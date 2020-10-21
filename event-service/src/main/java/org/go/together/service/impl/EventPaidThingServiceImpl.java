package org.go.together.service.impl;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.EventPaidThingDto;
import org.go.together.model.EventPaidThing;
import org.go.together.service.interfaces.EventPaidThingService;
import org.springframework.stereotype.Service;

@Service
public class EventPaidThingServiceImpl extends CrudServiceImpl<EventPaidThingDto, EventPaidThing>
        implements EventPaidThingService {
    @Override
    public String getServiceName() {
        return "eventPaidThing";
    }
}
