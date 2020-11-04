package org.go.together.test.client;

import org.go.together.base.FindClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.interfaces.Dto;
import org.springframework.stereotype.Component;

@Component
public class AnotherClient implements FindClient {
    private ResponseDto<Object> responseDto;

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return responseDto;
    }

    @Override
    public <T extends Dto> String validate(T routeInfo) {
        return null;
    }

    @Override
    public <T extends Dto> IdDto create(T dto) {
        return null;
    }

    @Override
    public <T extends Dto> IdDto update(T dto) {
        return null;
    }

    public void setResponseDto(ResponseDto<Object> responseDto) {
        this.responseDto = responseDto;
    }
}
