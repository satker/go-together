package org.go.together.test.client;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.test.dto.FakeDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnotherClient implements FindProducer<FakeDto> {
    private ResponseDto<Object> responseDto;

    public void setResponseDto(ResponseDto<Object> responseDto) {
        this.responseDto = responseDto;
    }

    @Override
    public ResponseDto<Object> find(UUID requestId, FormDto dto) {
        return responseDto;
    }

    @Override
    public String getConsumerId() {
        return null;
    }
}
