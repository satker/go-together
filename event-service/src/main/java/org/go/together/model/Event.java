package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "event", schema = "event_service")
public class Event extends NamedIdentifiedEntity {
    private int peopleCount;

    @Column(columnDefinition = "uuid")
    private UUID authorId;

    private String description;

    private Date startDate;

    private Date endDate;

    @Column(columnDefinition = "uuid")
    private UUID groupPhotoId;

    @Column(columnDefinition = "uuid")
    private UUID routeId;

}
