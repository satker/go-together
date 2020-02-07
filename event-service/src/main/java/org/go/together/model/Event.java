package org.go.together.model;

import lombok.Data;
import org.go.together.dto.HousingType;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "event", schema = "public")
public class Event implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private int peopleCount;
    private int like;
    private UUID authorId;
    private HousingType housingType;
    private String description;
    private Set<UUID> users;
    private Set<UUID> routes;
    private UUID eventPhotoId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private Set<EventPaidThing> paidThings;

}
