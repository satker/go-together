package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.dto.form.FormDto;
import org.go.together.kafka.impl.consumers.CommonCrudKafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GroupLocationConsumer extends CommonCrudKafkaConsumer<GroupLocationDto> {
    private final CrudService<GroupLocationDto> service;
    private final Validator<GroupLocationDto> validator;
    private final FindService<GroupLocationDto> findService;

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.LocationServiceInfo).GROUP_LOCATION.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).CREATE.getDescription())}",
            containerFactory = "groupLocationsChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, GroupLocationDto> message) {
        return service.create(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.LocationServiceInfo).GROUP_LOCATION.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).UPDATE.getDescription())}",
            containerFactory = "groupLocationsChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, GroupLocationDto> message) {
        return service.update(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.LocationServiceInfo).GROUP_LOCATION.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).DELETE.getDescription())}",
            containerFactory = "groupLocationsDeleteListenerContainerFactory")
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        service.delete(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.LocationServiceInfo).GROUP_LOCATION.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).READ.getDescription())}",
            containerFactory = "groupLocationsReadListenerContainerFactory")
    @SendTo
    public GroupLocationDto handleRead(ConsumerRecord<UUID, UUID> message) {
        return service.read(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.LocationServiceInfo).GROUP_LOCATION.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).VALIDATE.getDescription())}",
            containerFactory = "groupLocationsValidateListenerContainerFactory")
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<UUID, GroupLocationDto> message) {
        GroupLocationDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.LocationServiceInfo).GROUP_LOCATION.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).FIND.getDescription())}",
            containerFactory = "findListenerContainerFactory")
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.key(), message.value());
    }
}