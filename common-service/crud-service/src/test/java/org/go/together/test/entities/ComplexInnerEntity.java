package org.go.together.test.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.model.NamedIdentifiedEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ComplexInnerEntity extends NamedIdentifiedEntity {
}
