package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "language", schema = "user_service")
public class Language implements IdentifiedEntity {
    @Id
    private UUID id;
    private String name;
    private String code;
}
