package org.go.together.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.dto.EventUserStatus;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "event_user", schema = "event_service")
@AllArgsConstructor
@NoArgsConstructor
public class EventUser implements IdentifiedEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(columnDefinition = "uuid")
    private UUID userId;

    private EventUserStatus userStatus;

    @Column(columnDefinition = "uuid")
    private UUID eventId;
}
