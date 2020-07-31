package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class InterestDto implements Dto {
    private UUID id;
    @ComparingField(value = "name", isMain = true)
    private String name;
}
