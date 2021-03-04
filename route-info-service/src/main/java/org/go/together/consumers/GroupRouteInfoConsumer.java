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

import static org.go.together.enums.RouteInfoServiceInfo.GROUP_ROUTE_INFO;
import static org.go.together.enums.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class GroupRouteInfoConsumer extends CommonCrudKafkaConsumer<GroupRouteInfoDto> {
    private final CrudService<GroupRouteInfoDto> service;
    private final Validator<GroupRouteInfoDto> validator;
    private final FindService<GroupRouteInfoDto> findService;

    @Override
    @KafkaListener(topics = GROUP_ROUTE_INFO + CREATE,
            containerFactory = GROUP_ROUTE_INFO + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, GroupRouteInfoDto> message) {
        return service.create(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_ROUTE_INFO + UPDATE,
            containerFactory = GROUP_ROUTE_INFO + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, GroupRouteInfoDto> message) {
        return service.update(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_ROUTE_INFO + DELETE,
            containerFactory = GROUP_ROUTE_INFO + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        service.delete(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_ROUTE_INFO + READ,
            containerFactory = GROUP_ROUTE_INFO + READ + LISTENER_FACTORY)
    @SendTo
    public GroupRouteInfoDto handleRead(ConsumerRecord<UUID, UUID> message) {
        return service.read(message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_ROUTE_INFO + VALIDATE,
            containerFactory = GROUP_ROUTE_INFO + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<UUID, GroupRouteInfoDto> message) {
        GroupRouteInfoDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = GROUP_ROUTE_INFO + FIND,
            containerFactory = GROUP_ROUTE_INFO + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.value());
    }
}
