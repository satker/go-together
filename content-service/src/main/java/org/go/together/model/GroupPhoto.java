package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.PhotoCategory;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "group_photo", schema = "content_service")
public class GroupPhoto extends IdentifiedEntity {
    @Column(columnDefinition = "uuid")
    private UUID groupId;

    private PhotoCategory category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_photo_id")
    private Set<Photo> photos;
}
