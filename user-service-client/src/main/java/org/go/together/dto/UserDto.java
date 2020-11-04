package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"password"}, callSuper = true)
public class UserDto extends ComparableDto {
    @ComparingField("login")
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

    @Override
    public String getMainField() {
        return login;
    }
}
