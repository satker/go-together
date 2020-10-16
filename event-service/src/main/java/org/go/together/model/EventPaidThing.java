package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.CashCategory;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "event_paid_thing", schema = "event_service")
public class EventPaidThing extends IdentifiedEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    private CashCategory cashCategory;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "event_paid_thing_id", referencedColumnName = "id")
    private PaidThing paidThing;
}
