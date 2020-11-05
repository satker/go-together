package org.go.together.test.client;

import org.go.together.base.FindClient;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.dto.form.FormDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnotherClient implements FindClient {
    private ResponseDto<Object> responseDto;

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return responseDto;
    }

    @Override
    public ValidationMessageDto validate(String serviceName, Object dto) {
        return null;
    }

    @Override
    public IdDto create(String serviceName, Object dto) {
        return null;
    }

    @Override
    public IdDto update(String serviceName, Object dto) {
        return null;
    }

    @Override
    public void delete(String serviceName, UUID dtoId) {

    }

    @Override
    public Dto read(String serviceName, UUID dtoId) {
        return null;
    }

    public void setResponseDto(ResponseDto<Object> responseDto) {
        this.responseDto = responseDto;
    }
}
