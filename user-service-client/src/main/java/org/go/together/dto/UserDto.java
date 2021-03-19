package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

import java.util.Collection;
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
    private LocationDto location;

    @ComparingField("description")
    private String description;

    @ComparingField(value = "password", deepCompare = false)
    private String password;

    @ComparingField("role")
    private Role role;

    @ComparingField("user group photo")
    private GroupPhotoDto groupPhoto;

    @ComparingField("languages")
    private Collection<LanguageDto> languages;

    @ComparingField("interests")
    private Collection<InterestDto> interests;

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
