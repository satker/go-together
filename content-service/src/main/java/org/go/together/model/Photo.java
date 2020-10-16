package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "photo", schema = "content_service")
public class Photo extends IdentifiedEntity {
    private String photoUrl;
    private String contentType;
    private String pathName;
}
