package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class PaidThingDto implements Dto {
    private UUID id;

    @ComparingField(value = "paid thing name", isMain = true)
    private String name;
}
