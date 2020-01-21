package org.go.together.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private String id;
    private String login;
    private String mail;
    private String firstName;
    private String lastName;
    //private CityDto location;
    private String description;
    private String password;
    private Role role;
    //private PhotoDto userPhoto;
    private Set<String> languages;
}
