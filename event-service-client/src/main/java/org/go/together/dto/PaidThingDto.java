package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class PaidThingDto implements Dto {
    private UUID id;
    private String name;
}
