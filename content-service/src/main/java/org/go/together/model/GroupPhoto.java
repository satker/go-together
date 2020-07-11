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
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(columnDefinition = "uuid")
    private UUID groupId;

    private PhotoCategory category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_photo_id")
    private Set<Photo> photos;
}
