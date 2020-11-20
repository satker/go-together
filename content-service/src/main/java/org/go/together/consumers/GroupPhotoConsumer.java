package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.Validator;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.impl.consumers.CommonCrudKafkaConsumer;
import org.go.together.service.interfaces.GroupPhotoService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GroupPhotoConsumer extends CommonCrudKafkaConsumer<GroupPhotoDto> {
    private final GroupPhotoService groupPhotoService;
    private final Validator<GroupPhotoDto> validator;

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).CREATE.getDescription())}",
            containerFactory = "groupPhotosChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, GroupPhotoDto> message) {
        GroupPhotoDto dto = message.value();
        return groupPhotoService.create(dto);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).UPDATE.getDescription())}",
            containerFactory = "groupPhotosChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, GroupPhotoDto> message) {
        GroupPhotoDto dto = message.value();
        return groupPhotoService.update(dto);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).DELETE.getDescription())}")
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        UUID dtoId = message.value();
        groupPhotoService.delete(dtoId);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).READ.getDescription())}",
            groupId = "${kafka.groupId}",
            containerFactory = "groupPhotosReadListenerContainerFactory")
    @SendTo
    public Message<GroupPhotoDto> handleRead(ConsumerRecord<UUID, UUID> message) {
        UUID dtoId = message.value();
        return MessageBuilder.withPayload(groupPhotoService.read(dtoId))
                .setHeader(KafkaHeaders.MESSAGE_KEY, message.key())
                .build();
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).VALIDATE.getDescription())}")
    public String handleValidate(ConsumerRecord<UUID, GroupPhotoDto> message) {
        GroupPhotoDto dto = message.value();
        return validator.validate(dto, null);
    }
}
