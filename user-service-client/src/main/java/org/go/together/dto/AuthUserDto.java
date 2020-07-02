package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto implements ComparableDto {
    private UUID id;
    private String login;
    private String password;
    private Role role;
}
