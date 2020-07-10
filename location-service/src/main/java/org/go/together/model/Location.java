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
    private UUID id;
    private String name;
    private String state;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private Country country;
}
