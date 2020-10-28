package org.go.together.streams.interfaces;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface NotificationMessageStream {
    String INPUT = "notificationMessageInput";

    @Input(INPUT)
    SubscribableChannel notificationMessageChannel();
}
