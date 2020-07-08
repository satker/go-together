package org.go.together.model;

import lombok.Data;
import org.go.together.dto.PhotoCategory;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "group_photo", schema = "content_service")
public class GroupPhoto implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID groupId;
    private PhotoCategory category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_photo_id")
    private Set<Photo> photos;
}
