package org.go.together.test.client;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.kafka.producers.crud.FindKafkaProducer;
import org.go.together.test.dto.FakeDto;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnotherClient implements FindKafkaProducer<FakeDto> {
    private ResponseDto<Object> responseDto;

    public void setResponseDto(ResponseDto<Object> responseDto) {
        this.responseDto = responseDto;
    }

    @Override
    public String getGroupId() {
        return null;
    }

    @Override
    public ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> getReplyingKafkaTemplate() {
        return null;
    }

    @Override
    public String getTopicId() {
        return null;
    }

    @Override
    public ResponseDto<Object> find(UUID requestId, FormDto dto) {
        return responseDto;
    }
}
