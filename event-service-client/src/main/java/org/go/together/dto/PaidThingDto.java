package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
public class PaidThingDto implements ComparableDto {
    private UUID id;

    @ComparingField(value = "paid thing name", isMain = true)
    private String name;

    @Override
    public String toString() {
        return this.getName();
    }
}
