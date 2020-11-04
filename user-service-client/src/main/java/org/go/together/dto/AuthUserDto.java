package org.go.together.dto;

import lombok.*;
import org.go.together.interfaces.Dto;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto extends Dto {
    private String login;
    private String password;
    private Role role;
}
