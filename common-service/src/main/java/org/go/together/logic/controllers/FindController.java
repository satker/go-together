package org.go.together.logic.controllers;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.logic.services.CrudService;
import org.go.together.utils.FindUtils;

import java.util.List;
import java.util.stream.Collectors;

public abstract class FindController {
    private final List<CrudService> services;

    public FindController(List<CrudService> services) {
        this.services = services;
    }

    public ResponseDto<Object> find(FormDto formDto) {
        String[] serviceNameField = FindUtils.getSplitByDotString(formDto.getMainIdField());
        return services.stream()
                .filter(crudService -> crudService.getServiceName().equals(serviceNameField[0]))
                .collect(Collectors.toSet()).iterator().next().find(formDto);
    }
}
