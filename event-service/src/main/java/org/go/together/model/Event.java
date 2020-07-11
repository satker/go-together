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
@Table(name = "event", schema = "event_service")
public class Event implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true)
    private String name;

    private int peopleCount;

    @Column(columnDefinition = "uuid")
    private UUID authorId;

    private HousingType housingType;

    private String description;

    private Date startDate;

    private Date endDate;

    @Column(columnDefinition = "uuid")
    private UUID groupPhotoId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Set<EventUser> users;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Set<EventRoute> routes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Set<EventPaidThing> paidThings;

}
