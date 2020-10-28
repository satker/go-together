package org.go.together.service.impl;

import org.go.together.base.impl.CommonCrudService;
import org.go.together.dto.PaidThingDto;
import org.go.together.model.PaidThing;
import org.go.together.service.interfaces.PaidThingService;
import org.springframework.stereotype.Service;

@Service
public class PaidThingServiceImpl extends CommonCrudService<PaidThingDto, PaidThing> implements PaidThingService {
    @Override
    public String getServiceName() {
        return "paidThing";
    }
}