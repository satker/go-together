package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_message", schema = "notification_service")
@EqualsAndHashCode(exclude = {"notificationReceiverMessages"})
public class NotificationMessage implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(columnDefinition = "TEXT", length = 2048)
    private String message;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @OneToMany(mappedBy = "notificationMessage")
    private Set<NotificationReceiverMessage> notificationReceiverMessages;
}
