package org.go.together.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_receiver_message", schema = "public")
@NoArgsConstructor
public class NotificationReceiverMessage implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "notification_message_id", nullable = false)
    private NotificationMessage notificationMessage;

    public NotificationReceiverMessage(Boolean isRead, NotificationMessage notificationMessage) {
        this.isRead = isRead;
        this.notificationMessage = notificationMessage;
    }
}
