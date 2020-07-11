package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "event_route", schema = "event_service")
public class EventRoute implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(columnDefinition = "uuid")
    private UUID routeId;

    public EventRoute() {
    }

    public EventRoute(UUID routeId) {
        this.routeId = routeId;
    }
}
