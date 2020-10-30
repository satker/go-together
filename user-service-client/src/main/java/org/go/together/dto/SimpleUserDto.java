package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class SimpleUserDto extends ComparableDto {
    private UUID id;

    @ComparingField("login")
    private String login;

    @ComparingField("first name")
    private String firstName;

    @ComparingField("last name")
    private String lastName;

    @ComparingField("user photo")
    private PhotoDto userPhoto;

    @Override
    public String getMainField() {
        return login;
    }
}
