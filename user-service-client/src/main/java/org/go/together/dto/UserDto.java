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
    private LocationDto location;
    private String description;
    private String password;
    private Role role;
    private Set<PhotoDto> userPhotos;
    private Set<LanguageDto> languages;
    private Set<InterestDto> interests;
}
