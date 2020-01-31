package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDto implements Dto {
    private UUID id;
    private String login;
    private String mail;
    private String firstName;
    private String lastName;
    //private CityDto location;
    private String description;
    private String password;
    private Role role;
    //private PhotoDto userPhoto;
    private Set<LanguageDto> languages;
    private Set<InterestDto> interests;
}