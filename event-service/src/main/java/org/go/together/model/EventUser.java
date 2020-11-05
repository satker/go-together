package org.go.together.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.go.together.dto.EventUserStatus;

import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "event_user", schema = "event_service")
@AllArgsConstructor
@NoArgsConstructor
public class EventUser extends IdentifiedEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(columnDefinition = "uuid")
    private UUID userId;

    private EventUserStatus userStatus;

    @Column(columnDefinition = "uuid", name = "event_id")
    private UUID eventId;
}
