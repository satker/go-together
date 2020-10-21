package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.MessageDto;
import org.go.together.dto.MessageType;
import org.go.together.find.FindService;
import org.go.together.model.Message;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface MessageService extends CrudService<MessageDto>, FindService<Message> {
    Set<MessageDto> getReceiverMessages(UUID recipientId, MessageType messageType);

    Map<UUID, MessageDto> getAllChatsByEvent(UUID eventId);
}
