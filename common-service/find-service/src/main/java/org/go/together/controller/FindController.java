package org.go.together.controller;

import org.go.together.FindService;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.utils.FindUtils;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class FindController {
    private final Set<FindService> services;

    public FindController(Set<FindService> services) {
        this.services = services;
    }

    public ResponseDto<Object> find(FormDto formDto) {
        String[] serviceNameField = FindUtils.getSplitByDotString(formDto.getMainIdField());
        return services.stream()
                .filter(crudService -> crudService.getServiceName().equals(serviceNameField[0]))
                .collect(Collectors.toSet()).iterator().next().find(formDto);
    }
}
