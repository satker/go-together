package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "place", schema = "location_service")
public class Place extends NamedIdentifiedEntity {
    private String state;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Set<Location> locations;
}
