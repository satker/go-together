package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "notification_message", schema = "notification_service")
public class NotificationMessage extends IdentifiedEntity {
    @Column(columnDefinition = "TEXT", length = 2048)
    private String message;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
