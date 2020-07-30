package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
public class InterestDto implements ComparableDto {
    private UUID id;
    @ComparingField(value = "name", isMain = true)
    private String name;
}
