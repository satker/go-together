package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "location", schema = "location_service")
public class Location implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String address;
    private double latitude;
    private int routeNumber;
    private double longitude;

    private Boolean isStart;
    private Boolean isEnd;

    @ManyToOne
    @JoinColumn(columnDefinition = "uuid", name = "place_id")
    private Place place;
}
