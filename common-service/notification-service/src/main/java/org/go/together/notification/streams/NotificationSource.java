package org.go.together.notification.streams;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NotificationSource {
    String OUTPUT = "notificationMessageOutput";

    @Output(OUTPUT)
    MessageChannel output();
}
