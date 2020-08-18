package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.PaidThingDto;
import org.go.together.model.PaidThing;
import org.springframework.stereotype.Service;

@Service
public class PaidThingService extends CrudServiceImpl<PaidThingDto, PaidThing> {
    @Override
    public String getServiceName() {
        return "paidThing";
    }
}
