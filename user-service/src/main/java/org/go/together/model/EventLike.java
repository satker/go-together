package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "event_like", schema = "user_service")
public class EventLike extends IdentifiedEntity {
    @Column(unique = true, columnDefinition = "uuid")
    private UUID eventId;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(schema = "user_service",
            name = "system_user_event_like",
            joinColumns = @JoinColumn(name = "event_like_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<SystemUser> users;
}
