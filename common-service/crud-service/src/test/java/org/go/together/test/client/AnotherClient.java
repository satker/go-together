package org.go.together.test.client;

import org.go.together.find.client.FindClient;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
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
