package org.go.together.validation.test.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class ManyJoinDto implements Dto {
    private UUID id;
    private String name;
    private Long number;
}
