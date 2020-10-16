package org.go.together.repository.interfaces;

import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.CustomRepository;

import java.util.Collection;
import java.util.UUID;

public interface NotificationReceiverMessageRepository extends CustomRepository<NotificationReceiverMessage> {
    Collection<NotificationReceiverMessage> findByReceiverId(UUID receiverId);
}
