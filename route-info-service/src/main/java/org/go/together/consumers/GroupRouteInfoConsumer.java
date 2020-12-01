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

@Component
@RequiredArgsConstructor
public class GroupRouteInfoConsumer extends CommonCrudKafkaConsumer<GroupRouteInfoDto> {
    private final CrudService<GroupRouteInfoDto> service;
    private final Validator<GroupRouteInfoDto> validator;
    private final FindService<GroupRouteInfoDto> findService;

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.RouteInfoServiceInfo).GROUP_ROUTE_INFO.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).CREATE.getDescription())}",
            containerFactory = "groupRouteInfoChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, GroupRouteInfoDto> message) {
        return service.create(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.RouteInfoServiceInfo).GROUP_ROUTE_INFO.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).UPDATE.getDescription())}",
            containerFactory = "groupRouteInfoChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, GroupRouteInfoDto> message) {
        return service.update(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.RouteInfoServiceInfo).GROUP_ROUTE_INFO.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).DELETE.getDescription())}",
            containerFactory = "groupRouteInfoDeleteListenerContainerFactory")
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        service.delete(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.RouteInfoServiceInfo).GROUP_ROUTE_INFO.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).READ.getDescription())}",
            containerFactory = "groupRouteInfoReadListenerContainerFactory")
    @SendTo
    public GroupRouteInfoDto handleRead(ConsumerRecord<UUID, UUID> message) {
        return service.read(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.RouteInfoServiceInfo).GROUP_ROUTE_INFO.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).VALIDATE.getDescription())}",
            containerFactory = "groupRouteInfoValidateListenerContainerFactory")
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<UUID, GroupRouteInfoDto> message) {
        GroupRouteInfoDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.RouteInfoServiceInfo).GROUP_ROUTE_INFO.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).FIND.getDescription())}",
            containerFactory = "findListenerContainerFactory")
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.key(), message.value());
    }
}
