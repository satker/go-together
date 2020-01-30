package org.go.together.model;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private String id;
    private String login;
    private String mail;
    private String firstName;
    private String lastName;
    private String description;
    private String password;
    @Type(type = "uuid-char")
    private String locationId;
    @Type(type = "uuid-char")
    private String photoId;
    private String role;

    @ManyToMany
    private Set<Interest> interests = new HashSet<>();

    @ManyToMany
    private Set<Language> languages = new HashSet<>();
}
