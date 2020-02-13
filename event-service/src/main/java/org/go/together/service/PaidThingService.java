package org.go.together.service;

import org.go.together.dto.PaidThingDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.PaidThingMapper;
import org.go.together.model.PaidThing;
import org.go.together.repository.PaidThingRepository;
import org.go.together.validation.PaidThingValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PaidThingService extends CrudService<PaidThingDto, PaidThing> {
    private final PaidThingRepository paidThingRepository;
    private final PaidThingMapper paidThingMapper;

    protected PaidThingService(PaidThingRepository paidThingRepository,
                               PaidThingMapper paidThingMapper,
                               PaidThingValidator paidThingValidator) {
        super(paidThingRepository, paidThingMapper, paidThingValidator);
        this.paidThingMapper = paidThingMapper;
        this.paidThingRepository = paidThingRepository;
    }

    public Collection<PaidThingDto> getPaidThings() {
        return paidThingMapper.entitiesToDtos(paidThingRepository.findAll());
    }
}
