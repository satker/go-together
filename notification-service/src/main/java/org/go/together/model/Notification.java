package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "notification", schema = "notification_service")
public class Notification extends IdentifiedEntity {
    @Column(columnDefinition = "uuid")
    private UUID producerId;
}
