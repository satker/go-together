package org.go.together.repository.test.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class JoinTestEntity extends NamedIdentifiedEntity {
}
