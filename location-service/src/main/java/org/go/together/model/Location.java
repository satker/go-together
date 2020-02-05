package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "location", schema = "public")
public class Location implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String state;
    private Country country;
}
