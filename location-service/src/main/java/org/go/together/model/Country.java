package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "country", schema = "location_service")
public class Country extends NamedIdentifiedEntity {
    @Column(unique = true)
    private String countryCode;
}
