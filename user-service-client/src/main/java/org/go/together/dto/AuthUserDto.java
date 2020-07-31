package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto implements Dto {
    private UUID id;
    private String login;
    private String password;
    private Role role;
}
