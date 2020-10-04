package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "notification_receiver_message", schema = "notification_service")
public class NotificationReceiverMessage extends IdentifiedEntity {
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "notification_message_id")
    private NotificationMessage notificationMessage;

    @ManyToOne
    @JoinColumn(name = "notification_receiver_id")
    private NotificationReceiver notificationReceiver;
}
