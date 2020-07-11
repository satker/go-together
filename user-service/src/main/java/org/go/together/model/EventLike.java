package org.go.together.model;

import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "event_like", schema = "user_service")
public class EventLike implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true, columnDefinition = "uuid")
    private UUID eventId;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(schema = "user_service",
            name = "system_user_event_like",
            joinColumns = @JoinColumn(name = "event_like_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<SystemUser> users;
}
