package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "paid_thing", schema = "event_service")
public class PaidThing extends NamedIdentifiedEntity {
}
