package org.go.together.repository.interfaces;

import org.go.together.dto.MessageType;
import org.go.together.model.Message;
import org.go.together.repository.CustomRepository;

import java.util.Collection;
import java.util.UUID;

public interface MessageRepository extends CustomRepository<Message> {
    Collection<Message> findReviewsByRecipientId(UUID recipientId, MessageType messageType);

    Collection<Message> findReviewsByEventId(UUID recipientId, MessageType messageType);
}
