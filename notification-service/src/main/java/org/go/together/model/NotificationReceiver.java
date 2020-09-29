package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_receiver", schema = "notification_service")
public class NotificationReceiver implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(columnDefinition = "uuid")
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
