package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.dto.*;
import org.go.together.exceptions.ApplicationException;
import org.go.together.kafka.consumer.impl.CommonCrudKafkaConsumer;
import org.go.together.service.interfaces.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.go.together.enums.TopicKafkaPostfix.FIND;
import static org.go.together.enums.UserServiceInfo.AUTH_USER;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class AuthUserConsumer extends CommonCrudKafkaConsumer<AuthUserDto> {
    private final UserService service;

    @Override
    public IdDto handleCreate(ConsumerRecord<Long, AuthUserDto> message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IdDto handleUpdate(ConsumerRecord<Long, AuthUserDto> message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleDelete(ConsumerRecord<Long, UUID> message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AuthUserDto handleRead(ConsumerRecord<Long, UUID> message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidationMessageDto handleValidate(ConsumerRecord<Long, AuthUserDto> message) {
        throw new UnsupportedOperationException();
    }

    @Override
    @KafkaListener(topics = AUTH_USER + FIND,
            containerFactory = AUTH_USER + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message) {
        ResponseDto<Object> objectResponseDto = service.find(message.value());
        Collection<Object> result = objectResponseDto.getResult();
        if (result.isEmpty()) {
            throw new ApplicationException("Cannot get user by login");
        }
        String login = (String) result.iterator().next();
        AuthUserDto authUserByLogin = service.findAuthUserByLogin(login);
        objectResponseDto.setResult(Collections.singleton(authUserByLogin));
        return objectResponseDto;
    }
}