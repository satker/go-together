package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "event_location", schema = "public")
public class EventLocation implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String address;
    private double latitude;
    private int routeNumber;
    private double longitude;
    private UUID eventId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_location_id")
    private Location location;
}