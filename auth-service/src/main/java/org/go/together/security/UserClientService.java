package org.go.together.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.go.together.dto.*;
import org.go.together.enums.FindOperator;
import org.go.together.exceptions.ApplicationException;
import org.go.together.kafka.producers.FindProducer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static org.go.together.enums.UserServiceInfo.USERS;

@Service
@RequiredArgsConstructor
public class UserClientService {
    private static final String LOGIN = "login";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final FindProducer<AuthUserDto> findAuthUser;

    public AuthUserDto findAuthUserByLogin(String userName) {
        ResponseDto<Object> result = findResult(userName);
        return getAuthUserFromResponse(result);
    }

    private ResponseDto<Object> findResult(String userName) {
        FormDto formDto = new FormDto();
        formDto.setMainIdField(USERS + "." + LOGIN);
        FilterDto filterDto = new FilterDto();
        filterDto.setValues(Set.of(Map.of(LOGIN, new FilterValueDto(FindOperator.EQUAL, userName))));
        formDto.setFilters(Map.of(LOGIN, filterDto));
        return findAuthUser.find(formDto);
    }

    private AuthUserDto getAuthUserFromResponse(ResponseDto<Object> result) {
        try {
            Object notParsedAuthUser = result.getResult().iterator().next();
            String jsonAuthUser = OBJECT_MAPPER.writeValueAsString(notParsedAuthUser);
            return OBJECT_MAPPER.readValue(jsonAuthUser, AuthUserDto.class);
        } catch (Exception e) {
            throw new ApplicationException("Cannot parse auth user: " + e.getMessage());
        }
    }
}
