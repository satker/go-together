package org.go.together.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationReceiverMessageDto extends Dto {
    private Boolean isRead;
    private NotificationMessageDto notificationMessage;

    @JsonIgnore
    private NotificationReceiverDto notificationReceiver;
}
