package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class SimpleUserDto implements Dto {
    private UUID id;
    private String login;
    private String firstName;
    private String lastName;
    private PhotoDto userPhoto;
}
