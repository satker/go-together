package org.go.together.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.Dto;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationReceiverMessageDto extends Dto {
    private Boolean isRead;
    private NotificationMessageDto notificationMessage;

    @JsonIgnore
    private NotificationReceiverDto notificationReceiver;
}
