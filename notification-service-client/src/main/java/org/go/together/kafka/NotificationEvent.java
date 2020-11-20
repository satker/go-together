package org.go.together.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.go.together.dto.NotificationMessageDto;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private NotificationMessageDto message;
    private UUID producerId;
    private NotificationEventStatus status;
    private UUID receiverId;
}
