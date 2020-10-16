package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.MessageDto;
import org.go.together.dto.MessageType;
import org.go.together.enums.CrudOperation;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.Message;
import org.go.together.repository.interfaces.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService extends CrudServiceImpl<MessageDto, Message> {
    public Set<MessageDto> getReceiverMessages(UUID recipientId, MessageType messageType) {
        return ((MessageRepository) repository).findReviewsByRecipientId(recipientId, messageType).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Map<UUID, MessageDto> getAllChatsByEvent(UUID eventId) {
        List<Message> messagesByAuthorId = ((MessageRepository) repository).findReviewsByEventId(eventId, MessageType.TO_EVENT).stream()
                .collect(Collectors.groupingBy(Message::getAuthorId)).get(eventId);
        Map<UUID, List<Message>> groupAuthorEventId = Optional.ofNullable(messagesByAuthorId)
                .orElse(Collections.emptyList()).stream()
                .filter(message -> message.getRecipientId() != null)
                .collect(Collectors.groupingBy(Message::getRecipientId));
        List<Message> messagesByRecipientId = ((MessageRepository) repository).findReviewsByEventId(eventId, MessageType.TO_EVENT).stream()
                .filter(message -> message.getRecipientId() != null)
                .collect(Collectors.groupingBy(Message::getRecipientId)).get(eventId);
        Map<UUID, List<Message>> groupRecipientId = Optional.ofNullable(messagesByRecipientId)
                .orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(Message::getAuthorId));

        groupRecipientId.forEach((key, value) -> groupAuthorEventId.merge(key, value, (messages1, messages2) -> {
            messages1.addAll(messages2);
            return messages1;
        }));

        return groupAuthorEventId.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        element -> mapper.entityToDto(element.getValue().stream()
                                .max(Comparator.comparing(Message::getDate)).orElse(new Message()))));
    }

    @Override
    protected Message enrichEntity(Message entity, MessageDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            entity.setDate(new Date());
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "message";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("messageType", FieldMapper.builder()
                        .currentServiceField("messageType")
                        .fieldClass(MessageType.class).build())
                .put("recipientId", FieldMapper.builder()
                        .currentServiceField("recipientId")
                        .fieldClass(UUID.class).build())
                .put("authorId", FieldMapper.builder()
                        .currentServiceField("authorId")
                        .fieldClass(UUID.class).build()).build();
    }
}
