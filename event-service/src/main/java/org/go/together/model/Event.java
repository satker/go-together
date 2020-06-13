package org.go.together.model;

import lombok.Data;
import org.go.together.dto.HousingType;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "event", schema = "public")
public class Event implements IdentifiedEntity {
    @Id
    private UUID id;
    private String name;
    private int peopleCount;
    private UUID authorId;
    private HousingType housingType;
    private String description;
    private Date startDate;
    private Date endDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Set<EventUser> users;

    @ElementCollection
    @CollectionTable(name = "event_route", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "route_id")
    private Set<UUID> routes;

    private UUID eventPhotoId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private Set<EventPaidThing> paidThings;

}
