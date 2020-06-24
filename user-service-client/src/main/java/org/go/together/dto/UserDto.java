package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDto implements Dto {
    private UUID id;

    @ComparingField(value = "login", isMain = true)
    private String login;

    @ComparingField("mail")
    private String mail;

    @ComparingField("first name")
    private String firstName;

    @ComparingField("last name")
    private String lastName;

    @ComparingField("location")
    private LocationDto location;

    @ComparingField("description")
    private String description;

    @ComparingField(value = "password", deepCompare = false)
    private String password;

    @ComparingField("role")
    private Role role;

    @ComparingField("user photos")
    private Set<PhotoDto> userPhotos;

    @ComparingField("languages")
    private Set<LanguageDto> languages;

    @ComparingField("interests")
    private Set<InterestDto> interests;

    @Override
    public UUID getOwnerId() {
        return getId();
    }
}
