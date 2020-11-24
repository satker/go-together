package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.kafka.impl.consumers.CommonCrudKafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GroupPhotoConsumer extends CommonCrudKafkaConsumer<GroupPhotoDto> {
    private final CrudService<GroupPhotoDto> service;
    private final Validator<GroupPhotoDto> validator;
    private final FindService<GroupPhotoDto> findService;

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).CREATE.getDescription())}",
            containerFactory = "groupPhotosChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, GroupPhotoDto> message) {
        GroupPhotoDto dto = message.value();
        return service.create(dto);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).UPDATE.getDescription())}",
            containerFactory = "groupPhotosChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, GroupPhotoDto> message) {
        GroupPhotoDto dto = message.value();
        return service.update(dto);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).DELETE.getDescription())}",
            containerFactory = "groupPhotosDeleteListenerContainerFactory")
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        UUID dtoId = message.value();
        service.delete(dtoId);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).READ.getDescription())}",
            containerFactory = "groupPhotosReadListenerContainerFactory")
    @SendTo
    public GroupPhotoDto handleRead(ConsumerRecord<UUID, UUID> message) {
        UUID dtoId = message.value();
        return service.read(dtoId);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).VALIDATE.getDescription())}",
            containerFactory = "groupPhotosValidateListenerContainerFactory")
    @SendTo
    public String handleValidate(ConsumerRecord<UUID, GroupPhotoDto> message) {
        GroupPhotoDto dto = message.value();
        return validator.validate(dto, null);
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.ServiceInfo).GROUP_PHOTO_NAME.getDescription()" +
            ".concat(T(org.go.together.kafka.interfaces.TopicKafkaPostfix).FIND.getDescription())}",
            containerFactory = "groupPhotosFindListenerContainerFactory")
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.value());
    }
}
