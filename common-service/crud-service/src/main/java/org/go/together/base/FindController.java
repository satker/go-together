package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.find.utils.FindUtils;
import org.go.together.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FindController implements FindClient {
    private Map<String, FindService> services;
    private final Map<String, Class<? extends Dto>> serviceNameDtoClass = new HashMap<>();

    @Autowired
    public void setServices(Set<FindService> services) {
        this.services = services.stream()
                .collect(Collectors.toMap(FindService::getServiceName, findService -> {
                    serviceNameDtoClass.put(findService.getServiceName(), ReflectionUtils.getParametrizedClass(findService.getClass(),
                            0));
                    return findService;
                }));
    }

    public ResponseDto<Object> find(FormDto formDto) {
        String[] serviceNameField = FindUtils.getParsedFields(formDto.getMainIdField());
        return services.get(serviceNameField[0]).find(formDto);
    }
}
