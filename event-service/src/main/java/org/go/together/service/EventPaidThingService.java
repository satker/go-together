package org.go.together.service;

import org.go.together.CrudServiceImpl;
import org.go.together.dto.EventPaidThingDto;
import org.go.together.dto.FieldMapper;
import org.go.together.mapper.EventPaidThingMapper;
import org.go.together.model.EventPaidThing;
import org.go.together.repository.EventPaidThingRepository;
import org.go.together.validation.EventPaidThingValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventPaidThingService extends CrudServiceImpl<EventPaidThingDto, EventPaidThing> {
    protected EventPaidThingService(EventPaidThingRepository eventPaidThingRepository,
                                    EventPaidThingMapper eventPaidThingMapper,
                                    EventPaidThingValidator eventPaidThingValidator) {
        super(eventPaidThingRepository, eventPaidThingMapper, eventPaidThingValidator);
    }

    @Override
    public String getServiceName() {
        return "eventPaidThing";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
