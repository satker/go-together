package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.notification.dto.LocationCategory;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "group_location", schema = "location_service")
public class GroupLocation implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private UUID groupId;
    private LocationCategory category;

    @OneToMany(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "group_location_id")
    private Set<Location> locations;
}
