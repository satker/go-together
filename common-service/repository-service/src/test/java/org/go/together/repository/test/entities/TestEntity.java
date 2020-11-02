package org.go.together.repository.test.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.repository.entities.NamedIdentifiedEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class TestEntity extends NamedIdentifiedEntity {
    private long number;
    private Date date;
    private Date startDate;
    private Date endDate;
    private long startNumber;
    private long endNumber;
    private double latitude;
    private double longitude;
    private String simpleDto;

    @ElementCollection
    @CollectionTable(name = "test_elements", joinColumns = @JoinColumn(name = "test_id"))
    @Column(name = "element_id", columnDefinition = "uuid")
    private Set<UUID> elements;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "test_id")
    private Set<JoinTestEntity> joinTestEntities;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "test_many_join",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "many_join_id"))
    private Set<ManyJoinEntity> manyJoinEntities;
}
