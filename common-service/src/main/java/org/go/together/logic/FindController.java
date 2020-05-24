package org.go.together.logic;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;

import java.util.List;
import java.util.stream.Collectors;

public abstract class FindController {
    private final List<CrudService> services;

    public FindController(List<CrudService> services) {
        this.services = services;
    }

    public ResponseDto find(FormDto formDto) {
        String[] serviceNameField = formDto.getMainIdField().split("\\.", 2);
        return services.stream()
                .filter(crudService -> crudService.getServiceName().equals(serviceNameField[0]))
                .collect(Collectors.toSet()).iterator().next().find(formDto);
    }
}
