package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "place", schema = "location_service")
public class Place extends NamedIdentifiedEntity {
    private String state;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany
    @JoinColumn(name = "place_id")
    private Set<Location> locations;
}
