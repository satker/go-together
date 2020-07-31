package org.go.together.model;

import lombok.Data;
import org.go.together.enums.NotificationStatus;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification", schema = "notification_service")
public class Notification implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(columnDefinition = "uuid")
    private UUID producerId;
    private NotificationStatus status;
}
