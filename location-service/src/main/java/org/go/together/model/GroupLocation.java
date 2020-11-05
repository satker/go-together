package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.LocationCategory;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "group_location", schema = "location_service")
public class GroupLocation extends IdentifiedEntity {
    private UUID groupId;
    private LocationCategory category;

    @OneToMany(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "group_location_id")
    private Set<Location> locations;
}
