package org.go.together.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.go.together.dto.AuthUserDto;
import org.go.together.dto.FilterDto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.enums.FindOperator;
import org.go.together.exceptions.ApplicationException;
import org.go.together.kafka.producers.FindProducer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.go.together.enums.UserServiceInfo.USERS;

@Service
@RequiredArgsConstructor
public class UserClientService {
    private static final String LOGIN = "login";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final FindProducer<AuthUserDto> findAuthUser;

    public AuthUserDto findAuthUserByLogin(String userName) {
        UUID requestId = UUID.randomUUID();
        ResponseDto<Object> result = findResult(requestId, userName);
        return getAuthUserFromResponse(requestId, result);
    }

    private ResponseDto<Object> findResult(UUID requestId, String userName) {
        FormDto formDto = new FormDto();
        formDto.setMainIdField(USERS.getDescription() + "." + LOGIN);
        FilterDto filterDto = new FilterDto();
        filterDto.setFilterType(FindOperator.EQUAL);
        filterDto.setValues(Set.of(Map.of(LOGIN, userName)));
        formDto.setFilters(Map.of(LOGIN, filterDto));
        return findAuthUser.find(requestId, formDto);
    }

    private AuthUserDto getAuthUserFromResponse(UUID requestId, ResponseDto<Object> result) {
        try {
            Object notParsedAuthUser = result.getResult().iterator().next();
            String jsonAuthUser = OBJECT_MAPPER.writeValueAsString(notParsedAuthUser);
            return OBJECT_MAPPER.readValue(jsonAuthUser, AuthUserDto.class);
        } catch (Exception e) {
            throw new ApplicationException("Cannot parse auth user: " + e.getMessage(), requestId);
        }
    }
}
