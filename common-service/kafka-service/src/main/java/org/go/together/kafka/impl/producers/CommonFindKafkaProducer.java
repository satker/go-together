package org.go.together.kafka.impl.producers;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.kafka.interfaces.producers.crud.FindKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonFindKafkaProducer implements FindKafkaProducer {
    private ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> kafkaTemplate;

    @Value("${kafka.groupId}")
    private String groupId;

    @Override
    public String getGroupId() {
        return groupId;
    }

    public ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }

    public void setFindReplyingKafkaTemplate(ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
