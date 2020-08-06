package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.EventPaidThingDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.EventPaidThing;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventPaidThingService extends CrudServiceImpl<EventPaidThingDto, EventPaidThing> {
    @Override
    public String getServiceName() {
        return "eventPaidThing";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
