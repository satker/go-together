package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.HousingType;
import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "event", schema = "event_service")
public class Event extends NamedIdentifiedEntity {
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

    @Column(columnDefinition = "uuid")
    private UUID routeId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Set<EventPaidThing> paidThings;

}
