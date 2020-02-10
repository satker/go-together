package org.go.together.model;


import lombok.Data;
import org.go.together.dto.Role;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "system_user", schema = "public")
public class SystemUser implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String login;
    private String mail;
    private String firstName;
    private String lastName;
    private String description;
    private String password;
    private UUID locationId;
    private Role role;

    @ElementCollection
    private Collection<UUID> photoIds;

    @ManyToMany
    @JoinTable(name = "system_user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    private Set<Interest> interests = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "system_user_language",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Language> languages = new HashSet<>();
}
