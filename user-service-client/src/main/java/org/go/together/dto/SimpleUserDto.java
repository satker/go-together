package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class SimpleUserDto implements Dto {
    private UUID id;

    @ComparingField(value = "login", isMain = true)
    private String login;

    @ComparingField("first name")
    private String firstName;

    @ComparingField("last name")
    private String lastName;

    @ComparingField("user photo")
    private PhotoDto userPhoto;
}
