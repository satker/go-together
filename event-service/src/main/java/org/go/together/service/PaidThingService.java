package org.go.together.service;

import org.go.together.CrudServiceImpl;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.PaidThingDto;
import org.go.together.mapper.PaidThingMapper;
import org.go.together.model.PaidThing;
import org.go.together.repository.PaidThingRepository;
import org.go.together.validation.PaidThingValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaidThingService extends CrudServiceImpl<PaidThingDto, PaidThing> {
    protected PaidThingService(PaidThingRepository paidThingRepository,
                               PaidThingMapper paidThingMapper,
                               PaidThingValidator paidThingValidator) {
        super(paidThingRepository, paidThingMapper, paidThingValidator);
    }

    @Override
    public String getServiceName() {
        return "paidThing";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
