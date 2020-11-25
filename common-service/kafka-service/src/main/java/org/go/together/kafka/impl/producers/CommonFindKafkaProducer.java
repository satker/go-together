package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.kafka.interfaces.producers.crud.FindKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonFindKafkaProducer<D extends Dto> implements FindKafkaProducer {
    private final ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> kafkaTemplate;
    private final String groupId;

    protected CommonFindKafkaProducer(ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> kafkaTemplate,
                                      String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
