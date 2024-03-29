package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.FindController;
import org.go.together.client.MessageClient;
import org.go.together.dto.FormDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.MessageDto;
import org.go.together.dto.ResponseDto;
import org.go.together.service.interfaces.MessageService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.go.together.dto.MessageType.TO_EVENT;

@RestController
@RequiredArgsConstructor
public class MessageController extends FindController implements MessageClient {
    private final MessageService messageService;

    @Override
    public Map<UUID, MessageDto> getAllChatsByEvent(UUID eventId) {
        return messageService.getAllChatsByEvent(eventId);
    }

    @Override
    public Set<MessageDto> sentMessageToAnotherUser(UUID myId, UUID otherUser, MessageDto messageDto) {
        return null;
    }

    @Override
    public IdDto sentMessageToEvent(UUID eventId, MessageDto messageDto) {
        messageDto.setMessageType(TO_EVENT);
        return messageService.create(messageDto);
    }

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }

    @Override
    public Set<MessageDto> sentReviewToUser(UUID userId, MessageDto messageDto) {
        return null;
    }

    @Override
    public Set<MessageDto> updateMessageToAnotherUser(UUID myId, UUID otherUser, MessageDto messageDto) {
        return null;
    }

    @Override
    public Set<MessageDto> updateMessageToEvent(UUID eventId, MessageDto messageDto) {
        return null;
    }

    @Override
    public Set<MessageDto> updateReviewToUser(UUID userId, MessageDto messageDto) {
        return null;
    }

    @Override
    public Set<MessageDto> deleteMessageToAnotherUser(UUID myId, UUID otherUser, MessageDto messageDto) {
        return null;
    }

    @Override
    public Set<MessageDto> deleteMessageToEvent(UUID eventId, MessageDto messageDto) {
        return null;
    }

    @Override
    public Set<MessageDto> deleteReviewToUser(UUID userId, MessageDto messageDto) {
        return null;
    }
}
