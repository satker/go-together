package org.go.together.notification.helpers.interfaces;

import java.util.UUID;

public interface ReceiverSender {
    void send(UUID id, UUID producerId, UUID receiverId);
}
