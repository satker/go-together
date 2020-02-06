package org.go.together.model;

import lombok.Data;
import org.go.together.dto.PhotoCategory;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "photo", schema = "public")
public class Photo implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String photoUrl;
    private String contentType;
    private String pathName;
    private PhotoCategory category;
}
