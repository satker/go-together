package org.go.together.model;

import lombok.Data;
import org.go.together.dto.NotificationStatus;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification", schema = "public")
public class Notification implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID producerId;
    private NotificationStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_message_id")
    private Set<NotificationMessage> notificationMessages;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_receiver_id")
    private Set<NotificationReceiver> notificationReceivers;
}
