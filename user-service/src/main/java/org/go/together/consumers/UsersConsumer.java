package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.*;
import org.go.together.kafka.consumer.impl.CommonCrudKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.go.together.enums.TopicKafkaPostfix.*;
import static org.go.together.enums.UserServiceInfo.EVENT_LIKES;
import static org.go.together.enums.UserServiceInfo.USERS;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class UsersConsumer extends CommonCrudKafkaConsumer<UserDto> {
    @Override
    @KafkaListener(topics = USERS + CREATE,
            containerFactory = USERS + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, UserDto> message) {
        return service.create(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = USERS + UPDATE,
            containerFactory = USERS + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, UserDto> message) {
        return service.update(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = USERS + DELETE,
            containerFactory = USERS + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        service.delete(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = USERS + READ,
            containerFactory = USERS + READ + LISTENER_FACTORY)
    @SendTo
    public UserDto handleRead(ConsumerRecord<UUID, UUID> message) {
        return service.read(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = USERS + VALIDATE,
            containerFactory = USERS + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<UUID, UserDto> message) {
        UserDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = USERS + FIND,
            containerFactory = USERS + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.key(), message.value());
    }
}
