package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "paid_thing", schema = "event_service")
public class PaidThing implements IdentifiedEntity {
    @Id
    private UUID id;
    private String name;
}
