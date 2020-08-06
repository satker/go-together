package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"password"})
public class UserDto implements ComparableDto, Dto {
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
    private GroupLocationDto location;

    @ComparingField("description")
    private String description;

    @ComparingField(value = "password", deepCompare = false)
    private String password;

    @ComparingField("role")
    private Role role;

    @ComparingField("user group photo")
    private GroupPhotoDto groupPhoto;

    @ComparingField("languages")
    private Set<LanguageDto> languages;

    @ComparingField("interests")
    private Set<InterestDto> interests;

    @Override
    public UUID getOwnerId() {
        return getId();
    }

    @Override
    public UUID getParentId() {
        return getId();
    }
}
