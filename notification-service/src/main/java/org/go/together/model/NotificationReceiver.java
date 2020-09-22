package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_receiver", schema = "notification_service")
@EqualsAndHashCode(exclude = {"notificationReceiverMessages"})
public class NotificationReceiver implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(columnDefinition = "uuid")
    private UUID userId;

    @OneToMany(mappedBy = "notificationReceiver")
    private Set<NotificationReceiverMessage> notificationReceiverMessages;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
