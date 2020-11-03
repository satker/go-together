package org.go.together.base.impl;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.find.FindService;
import org.go.together.find.utils.FindUtils;
import org.go.together.interfaces.Dto;
import org.go.together.utils.ReflectionUtils;
import org.go.together.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FindController {
    private Set<FindService<?>> services;
    private Map<Class<?>, Validator> validators;

    @Autowired
    public void setServices(Set<FindService<?>> services) {
        this.services = services;
    }

    @Autowired
    public void setValidators(Set<Validator<?>> validators) {
        this.validators = validators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedInterface(comparator.getClass(), 0),
                        Function.identity()));
    }

    public ResponseDto<Object> find(FormDto formDto) {
        String[] serviceNameField = FindUtils.getParsedFields(formDto.getMainIdField());
        return services.stream()
                .filter(crudService -> crudService.getServiceName().equals(serviceNameField[0]))
                .collect(Collectors.toSet()).iterator().next().find(formDto);
    }

    public <T extends Dto> String validate(T dto) {
        return validators.get(dto.getClass()).validate(dto, null);
    }
}
