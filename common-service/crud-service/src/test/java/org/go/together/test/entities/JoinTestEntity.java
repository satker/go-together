package org.go.together.test.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.model.NamedIdentifiedEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class JoinTestEntity extends NamedIdentifiedEntity {
    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "join_test_id")
    private ComplexInnerEntity complexInner;
}
