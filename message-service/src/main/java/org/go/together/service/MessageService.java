package org.go.together.service;

import org.go.together.dto.MessageDto;
import org.go.together.dto.MessageType;
import org.go.together.logic.CrudService;
import org.go.together.mapper.MessageMapper;
import org.go.together.model.Message;
import org.go.together.repository.MessageRepository;
import org.go.together.validation.MessageValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService extends CrudService<MessageDto, Message> {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    protected MessageService(MessageRepository messageRepository,
                             MessageMapper messageMapper,
                             MessageValidator messageValidator) {
        super(messageRepository, messageMapper, messageValidator);
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public Set<MessageDto> getReceiverMessages(UUID recipientId, MessageType messageType) {
        return messageRepository.findReviewsByRecipientId(recipientId, messageType).stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Set<MessageDto> getChatBetweenUsers(UUID myId, UUID otherUser) {
        return messageRepository.findMessagesBetweenUsers(myId, otherUser).stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Map<UUID, List<MessageDto>> getAllChatsByUserId(UUID myId) {
        return messageRepository.findLastMessagesForUserId(myId).entrySet().stream()
                .map(ob -> messageMapper.entitiesToDtos(ob.getValue()))
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(MessageDto::getRecipientId));
    }
}
