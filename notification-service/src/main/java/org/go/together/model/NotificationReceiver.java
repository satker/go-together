package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_receiver", schema = "notification_service")
public class NotificationReceiver implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_receiver_id")
    private Set<NotificationReceiverMessage> notificationReceiverMessages = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;
}
