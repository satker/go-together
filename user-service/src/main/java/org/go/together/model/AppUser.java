package org.go.together.model;


import lombok.Data;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class AppUser implements IdentifiedEntity {
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
    private UUID photoId;
    private String role;

    @ManyToMany
    private Set<Interest> interests = new HashSet<>();

    @ManyToMany
    private Set<Language> languages = new HashSet<>();
}
