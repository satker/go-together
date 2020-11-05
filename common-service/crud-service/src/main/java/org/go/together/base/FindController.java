package org.go.together.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.dto.form.FormDto;
import org.go.together.find.utils.FindUtils;
import org.go.together.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FindController implements FindClient {
    private Map<String, FindService> services;
    private Map<Class<?>, Validator> validators;
    private Map<Class<?>, CrudService> crudServices;
    private final Map<String, Class<? extends Dto>> serviceNameDtoClass = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setServices(Set<FindService> services) {
        this.services = services.stream()
                .collect(Collectors.toMap(FindService::getServiceName, findService -> {
                    serviceNameDtoClass.put(findService.getServiceName(), ReflectionUtils.getParametrizedClass(findService.getClass(),
                            0));
                    return findService;
                }));
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
    public ValidationMessageDto validate(String serviceName, Object dto) {
        Class<? extends Dto> dtoClazz = getDtoClass(serviceName);
        Dto parsedDto = checkDtoType(dtoClazz, dto);
        String validationMessage = validators.get(dtoClazz).validate(parsedDto, null);
        return new ValidationMessageDto(validationMessage);
    }

    @Override
    public IdDto create(String serviceName, Object dto) {
        Class<? extends Dto> dtoClazz = getDtoClass(serviceName);
        Dto parsedDto = checkDtoType(dtoClazz, dto);
        return crudServices.get(dtoClazz).create(parsedDto);
    }

    @Override
    public IdDto update(String serviceName, Object dto) {
        Class<? extends Dto> dtoClazz = getDtoClass(serviceName);
        Dto parsedDto = checkDtoType(dtoClazz, dto);
        return crudServices.get(dtoClazz).update(parsedDto);
    }

    @Override
    public void delete(String serviceName, UUID dtoId) {
        Class<? extends Dto> dtoClazz = getDtoClass(serviceName);
        crudServices.get(dtoClazz).delete(dtoId);
    }

    @Override
    public <D extends Dto> D read(String serviceName, UUID dtoId) {
        Class<? extends Dto> dtoClazz = getDtoClass(serviceName);
        return (D) crudServices.get(dtoClazz).read(dtoId);
    }

    private Dto checkDtoType(Class<? extends Dto> dtoClazz, Object dto) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(dto), dtoClazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Incorrect dto");
        }
    }

    private Class<? extends Dto> getDtoClass(String serviceName) {
        Class<? extends Dto> dtoClazz = serviceNameDtoClass.get(serviceName);
        if (dtoClazz == null) {
            throw new IllegalArgumentException("Incorrect service name" + serviceName);
        }
        return dtoClazz;
    }
}
