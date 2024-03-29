package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "location", schema = "location_service")
public class Location extends IdentifiedEntity {
    private String address;
    private double latitude;
    private double longitude;
}
