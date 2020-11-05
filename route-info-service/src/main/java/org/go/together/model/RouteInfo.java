package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.TransportType;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "route_info", schema = "route_info_service")
public class RouteInfo extends IdentifiedEntity {
    private UUID startLocationId;
    private UUID endLocationId;
    private TransportType transportType;
    private Double cost;
    private Date movementDate;
    private Double movementDuration;
}
