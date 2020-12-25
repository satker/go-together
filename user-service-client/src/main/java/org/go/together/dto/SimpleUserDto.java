package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleUserDto extends ComparableDto {
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
