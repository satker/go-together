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
@Table(name = "country", schema = "public")
public class Country implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String countryCode;
    private String name;
}
