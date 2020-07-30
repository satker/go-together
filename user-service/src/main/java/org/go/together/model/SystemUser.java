package org.go.together.model;


import lombok.Data;
import org.go.together.dto.Role;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "system_user", schema = "user_service")
public class SystemUser implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(unique = true)
    private String login;
    @Column(unique = true)
    private String mail;
    private String firstName;
    private String lastName;
    private String description;
    private String password;

    @Column(columnDefinition = "uuid")
    private UUID locationId;
    private Role role;

    @Column(columnDefinition = "uuid")
    private UUID groupPhoto;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(schema = "user_service",
            name = "system_user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    private Set<Interest> interests = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(schema = "user_service",
            name = "system_user_language",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Language> languages = new HashSet<>();
}
