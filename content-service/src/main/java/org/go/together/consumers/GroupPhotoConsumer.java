package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.*;
import org.go.together.kafka.consumer.impl.CommonCrudKafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.go.together.enums.ContentServiceInfo.GROUP_PHOTO_NAME;
import static org.go.together.enums.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class GroupPhotoConsumer extends CommonCrudKafkaConsumer<GroupPhotoDto> {
    private final CrudService<GroupPhotoDto> service;
    private final Validator<GroupPhotoDto> validator;
    private final FindService<GroupPhotoDto> findService;

    @Override
    @KafkaListener(topics = GROUP_PHOTO_NAME + CREATE,
            containerFactory = GROUP_PHOTO_NAME + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<Long, GroupPhotoDto> message) {
        return service.create(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_PHOTO_NAME + UPDATE,
            containerFactory = GROUP_PHOTO_NAME + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<Long, GroupPhotoDto> message) {
        return service.update(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_PHOTO_NAME + DELETE,
            containerFactory = GROUP_PHOTO_NAME + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<Long, UUID> message) {
        service.delete(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_PHOTO_NAME + READ,
            containerFactory = GROUP_PHOTO_NAME + READ + LISTENER_FACTORY)
    @SendTo
    public GroupPhotoDto handleRead(ConsumerRecord<Long, UUID> message) {
        return service.read(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_PHOTO_NAME + VALIDATE,
            containerFactory = GROUP_PHOTO_NAME + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<Long, GroupPhotoDto> message) {
        GroupPhotoDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = GROUP_PHOTO_NAME + FIND,
            containerFactory = GROUP_PHOTO_NAME + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message) {
        return findService.find(message.value());
    }
}
