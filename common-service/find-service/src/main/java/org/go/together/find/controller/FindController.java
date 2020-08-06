package org.go.together.find.controller;

import org.go.together.find.FindService;
import org.go.together.find.dto.FormDto;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.utils.FindUtils;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class FindController {
    private final Set<FindService> services;

    public FindController(Set<FindService> services) {
        this.services = services;
    }

    public ResponseDto<Object> find(FormDto formDto) {
        String[] serviceNameField = FindUtils.getParsedFields(formDto.getMainIdField());
        return services.stream()
                .filter(crudService -> crudService.getServiceName().equals(serviceNameField[0]))
                .collect(Collectors.toSet()).iterator().next().find(formDto);
    }
}
