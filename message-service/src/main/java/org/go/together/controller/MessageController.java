package org.go.together.controller;

import org.go.together.client.MessageClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.MessageDto;
import org.go.together.dto.NotificationDto;
import org.go.together.service.MessageService;
import org.go.together.service.NotificationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.go.together.dto.MessageType.REVIEW;
import static org.go.together.dto.MessageType.TO_EVENT;

@RestController
public class MessageController implements MessageClient {
    private final MessageService messageService;
    private final NotificationService notificationService;

    public MessageController(MessageService messageService, NotificationService notificationService) {
        this.messageService = messageService;
        this.notificationService = notificationService;
    }

    @Override
    public Set<NotificationDto> getNotifications(UUID userId) {
        return null;
    }

    @Override
    public Set<NotificationDto> readNotifications(UUID userId) {
        return null;
    }

    @Override
    public Set<MessageDto> getUserReviews(UUID userId) {
        return messageService.getReceiverMessages(userId, REVIEW);
    }

    @Override
    public Set<MessageDto> getEventMessages(UUID eventId) {
        return messageService.getReceiverMessages(eventId, TO_EVENT);
    }

    @Override
    public Set<MessageDto> getChatBetweenUsers(UUID myId, UUID otherUser) {
        return messageService.getChatBetweenUsers(myId, otherUser);
    }

    @Override
    public Map<UUID, List<MessageDto>> getAllChats(UUID myId) {
        return messageService.getAllChatsByUserId(myId);
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
