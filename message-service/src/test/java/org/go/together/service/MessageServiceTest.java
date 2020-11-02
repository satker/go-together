package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.MessageDto;
import org.go.together.dto.MessageType;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Message;
import org.go.together.notification.streams.NotificationSource;
import org.go.together.service.interfaces.MessageService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
public class MessageServiceTest extends CrudServiceCommonTest<Message, MessageDto> {
    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationSource source;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        MessageChannel messageChannel = Mockito.mock(MessageChannel.class);
        when(source.output()).thenReturn(messageChannel);
        when(messageChannel.send(any())).thenReturn(true);
    }

    @Test
    void testGetReceiverMessages() {
        MessageDto messageDto = getCreatedEntityId(dto);

        Set<MessageDto> receiverMessages =
                messageService.getReceiverMessages(messageDto.getRecipientId(),
                        messageDto.getMessageType());

        assertEquals(1, receiverMessages.size());
        checkDtos(messageDto, receiverMessages.iterator().next(), null);
    }

    @Test
    void getAllChatsByEvent() {
        dto.setMessageType(MessageType.TO_EVENT);
        MessageDto messageDto = getCreatedEntityId(dto);

        Map<UUID, MessageDto> receiverMessages =
                messageService.getAllChatsByEvent(messageDto.getAuthorId());

        assertEquals(1, receiverMessages.size());
        checkDtos(messageDto, receiverMessages.get(messageDto.getRecipientId()), null);
    }

    @Test
    void getAllNotPresentedChatsByEvent() {
        Map<UUID, MessageDto> receiverMessages =
                messageService.getAllChatsByEvent(UUID.randomUUID());

        assertEquals(0, receiverMessages.size());
    }

    @Override
    protected MessageDto createDto() {
        MessageDto messageDto = factory.manufacturePojo(MessageDto.class);
        messageDto.setRating((double) generateLong(0, 5));
        return messageDto;
    }

    @Override
    protected void checkDtos(MessageDto dto, MessageDto savedObject, CrudOperation operation) {
        assertEquals(dto.getId(), savedObject.getId());
        assertEquals(dto.getMessage(), savedObject.getMessage());
        assertEquals(dto.getMessageType(), savedObject.getMessageType());
        assertEquals(dto.getAuthorId(), savedObject.getAuthorId());
        assertEquals(dto.getRating(), savedObject.getRating());
        assertEquals(dto.getRecipientId(), savedObject.getRecipientId());
    }
}