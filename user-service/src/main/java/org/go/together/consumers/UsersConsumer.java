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

import static org.go.together.enums.UserServiceInfo.USERS;
import static org.go.together.interfaces.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class UsersConsumer extends CommonCrudKafkaConsumer<UserDto> {
    private final CrudService<UserDto> service;
    private final Validator<UserDto> validator;
    private final FindService<UserDto> findService;

    @Override
    @KafkaListener(topics = USERS + CREATE,
            containerFactory = USERS + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<Long, UserDto> message) {
        return service.create(message.value());
    }

    @Override
    @KafkaListener(topics = USERS + UPDATE,
            containerFactory = USERS + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<Long, UserDto> message) {
        return service.update(message.value());
    }

    @Override
    @KafkaListener(topics = USERS + DELETE,
            containerFactory = USERS + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<Long, UUID> message) {
        service.delete(message.value());
    }

    @Override
    @KafkaListener(topics = USERS + READ,
            containerFactory = USERS + READ + LISTENER_FACTORY)
    @SendTo
    public UserDto handleRead(ConsumerRecord<Long, UUID> message) {
        return service.read(message.value());
    }

    @Override
    @KafkaListener(topics = USERS + VALIDATE,
            containerFactory = USERS + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<Long, UserDto> message) {
        UserDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = USERS + FIND,
            containerFactory = USERS + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message) {
        return findService.find(message.value());
    }
}
