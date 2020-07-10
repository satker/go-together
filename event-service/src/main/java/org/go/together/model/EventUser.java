package org.go.together.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.dto.EventUserStatus;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "event_user", schema = "event_service")
@AllArgsConstructor
@NoArgsConstructor
public class EventUser implements IdentifiedEntity {
    @Id
    private UUID id;
    private UUID userId;
    private EventUserStatus userStatus;
    @Column(name = "event_id")
    private UUID eventId;
}
