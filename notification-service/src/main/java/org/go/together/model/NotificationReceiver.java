package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "notification_receiver", schema = "notification_service")
public class NotificationReceiver extends IdentifiedEntity {
    @Column(columnDefinition = "uuid")
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
