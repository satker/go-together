package org.go.together.model;

import lombok.Data;
import org.go.together.dto.CashCategory;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "event_paid_thing", schema = "event_service")
public class EventPaidThing implements IdentifiedEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private CashCategory cashCategory;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "event_paid_thing_id", referencedColumnName = "id")
    private PaidThing paidThing;
}
