package org.go.together.base.impl;

import org.go.together.base.CrudService;
import org.go.together.base.FindClient;
import org.go.together.base.FindService;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.find.utils.FindUtils;
import org.go.together.interfaces.Dto;
import org.go.together.utils.ReflectionUtils;
import org.go.together.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FindController implements FindClient {
    private Map<String, FindService> services;
    private Map<Class<?>, Validator> validators;
    private Map<Class<?>, CrudService> crudServices;

    @Autowired
    public void setServices(Set<FindService> services) {
        this.services = services.stream()
                .collect(Collectors.toMap(FindService::getServiceName, Function.identity()));
    }

    @Autowired
    public void setValidators(Set<Validator<?>> validators) {
        this.validators = validators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedClass(comparator.getClass(), 0),
                        Function.identity()));
    }

    @Autowired
    public void setCrudServices(Set<CrudService<?>> crudServices) {
        this.crudServices = crudServices.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedClass(comparator.getClass(), 0),
                        Function.identity()));
    }

    public ResponseDto<Object> find(FormDto formDto) {
        String[] serviceNameField = FindUtils.getParsedFields(formDto.getMainIdField());
        return services.get(serviceNameField[0]).find(formDto);
    }

    @Override
    public <T extends Dto> String validate(T dto) {
        return validators.get(dto.getClass()).validate(dto, null);
    }

    @Override
    public <T extends Dto> IdDto create(T dto) {
        return crudServices.get(dto.getClass()).create(dto);
    }

    @Override
    public <T extends Dto> IdDto update(T dto) {
        return crudServices.get(dto.getClass()).update(dto);
    }
}
