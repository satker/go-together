package org.go.together.notification.helpers.interfaces;

import java.util.UUID;

public interface ReceiverSender {
    void send(UUID producerId, UUID receiverId);
}
