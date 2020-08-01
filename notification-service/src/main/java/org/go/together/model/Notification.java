package org.go.together.model;

import lombok.Data;
import org.go.together.enums.NotificationStatus;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification", schema = "notification_service")
public class Notification implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(columnDefinition = "uuid")
    private UUID producerId;
    private NotificationStatus status;
}
