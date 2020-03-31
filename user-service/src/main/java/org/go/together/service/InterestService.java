package org.go.together.service;

import org.go.together.dto.InterestDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.InterestMapper;
import org.go.together.model.Interest;
import org.go.together.repository.InterestRepository;
import org.go.together.validation.InterestValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class InterestService extends CrudService<InterestDto, Interest> {
    private final InterestRepository interestRepository;
    private final InterestMapper interestMapper;

    protected InterestService(InterestRepository interestRepository,
                              InterestMapper interestMapper,
                              InterestValidator interestValidator) {
        super(interestRepository, interestMapper, interestValidator);
        this.interestRepository = interestRepository;
        this.interestMapper = interestMapper;
    }

    public Collection<InterestDto> getInterests() {
        return interestMapper.entitiesToDtos(interestRepository.findAll());
    }
}