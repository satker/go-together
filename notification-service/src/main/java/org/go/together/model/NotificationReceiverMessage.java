package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_receiver_message", schema = "notification_service")
public class NotificationReceiverMessage implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "notification_message_id")
    private NotificationMessage notificationMessage;

    @ManyToOne
    @JoinColumn(name = "notification_receiver_id")
    private NotificationReceiver notificationReceiver;
}
