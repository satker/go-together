package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "group_route_info", schema = "route_info_service")
public class GroupRouteInfo extends IdentifiedEntity {
    private UUID groupId;

    @OneToMany(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "group_route_info_id")
    private Set<RouteInfo> infoRoutes;
}
