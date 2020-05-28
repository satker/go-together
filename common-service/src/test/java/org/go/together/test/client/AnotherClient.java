package org.go.together.test.client;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.filter.FormDto;
import org.go.together.interfaces.FindClient;
import org.springframework.stereotype.Component;

@Component
public class AnotherClient implements FindClient {
    private ResponseDto<Object> responseDto;

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return responseDto;
    }

    public void setResponseDto(ResponseDto<Object> responseDto) {
        this.responseDto = responseDto;
    }
}
