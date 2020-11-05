package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.NotificationReceiverMessage;

import java.util.Collection;
import java.util.UUID;

public interface NotificationReceiverMessageRepository extends CustomRepository<NotificationReceiverMessage> {
    Collection<NotificationReceiverMessage> findByReceiverId(UUID receiverId);
}
