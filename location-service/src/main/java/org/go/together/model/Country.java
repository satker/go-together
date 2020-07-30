package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "country", schema = "location_service")
public class Country implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true)
    private String countryCode;

    @Column(unique = true)
    private String name;
}
